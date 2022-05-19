package guc.bttsBtngan.post.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import guc.bttsBtngan.notification.commands.*;

import guc.bttsBtngan.post.data.Comment;
import guc.bttsBtngan.post.data.Comment.CommentVote;
import guc.bttsBtngan.post.data.Post;
import guc.bttsBtngan.post.data.Post.PostVote;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.mongodb.client.result.UpdateResult;



@Service
public class PostService {
    @Autowired
    MongoOperations mongoOperations;
    @Autowired
    private AmqpTemplate amqpTemplate;
    
    public String createPost(Post post)  {
    	post.setModeratorId("3amo moderator2");
        mongoOperations.save(post);
        
        
        // check if the user who posted the post has followers
        // if the list contains followers, we do the notification step (create hash map with Type,list<string>)
            
        HashMap<String, Object> type_IDs= new HashMap<String, Object>();
	  	ArrayList<String> btngan = new ArrayList<String>();  ;
	  	type_IDs.put("type", "post");
	  	type_IDs.put("userIDs", btngan);
        amqpTemplate.convertAndSend("notification_req",type_IDs,  m -> {
            m.getMessageProperties().setHeader("command", "createNotificationCommand");
            return m;
        });
        
     /////////////////////////////////////////////////////////////////////////////   
      
      
        return "DONE, created post is: "+(post).toString();
    }
    
    public String followPost(String userId, String postId)throws InterruptedException, ExecutionException {
    	Query query = new Query(Criteria.where("_id").is(postId));
    	Post post = mongoOperations.findOne(query, Post.class, "post");  	
    	
    	if(post.getPostFollowers() == null)
    	{
    		post.setPostFollowers(new ArrayList<String>());
    	}
    	post.addPostFollower(userId);
    	
    	Update update = new Update().set("postFollowers", post.getPostFollowers());
        mongoOperations.updateFirst(query, update, Post.class);
    	
    	return "DONE, Potatoes follow post : "+(post).toString();
    	
    }

    public String reportPost(String userId, String postId, String reportComment)throws InterruptedException, ExecutionException {
    	Query query = new Query(Criteria.where("_id").is(postId));
    	Post post = mongoOperations.findOne(query, Post.class, "post");

    	Post.PostReport postReport = new Post.PostReport(userId, reportComment);

    	if(post.getPostReports() == null)
    	{
    		post.setPostReports(new ArrayList<Post.PostReport>());
    	}
    	post.addPostReport(postReport);
    	
    	Update update = new Update().set("postReports", post.getPostReports());
        mongoOperations.updateFirst(query, update, Post.class);

    	return "DONE, Potatoes report post : "+(post).toString();

    }

    
    public String commentPost(String userId, String postId, String comment)throws InterruptedException, ExecutionException {
    	Query query = new Query();
    	query.addCriteria(Criteria.where("_id").is(postId));
    	Post post = mongoOperations.findById(query, Post.class, "post");

    	Comment postComment = new Comment(userId, comment);

    	post.getComments().add(postComment);

    	mongoOperations.save(post);

    	return "DONE, Potatoes comment post : "+(postComment).toString();

    }
    
 
    
    public String commentVote(String userId, String postId, String commentId,boolean vote)throws InterruptedException, ExecutionException {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(postId));
		Post post = mongoOperations.findById(query, Post.class, "post");
		if(post==null){
			return "post id is not valid";
		}
//		Comment cmnt;
//		for(Comment cmnt2: post.getComments()) {
//			cmnt2.getCommen
//		}
//		todo search for comment
		Comment cmnt = post.getComments().get(0);//TODO: search for comment by commentId
		boolean found =false;
		for(CommentVote cv: cmnt.getCommentVotes() ) {
			if(cv.getVoterId().equals(userId)) {
				found =true;
				if(cv.isUpVote()==vote) {
					cmnt.delCommentVote(cv);
				}
				else {
					cv.setUpVote(vote);
	
				}
				
			}
		}
		if(!found) {
			CommentVote cv=new CommentVote(userId,vote);
			cmnt.addCommentVote(cv);
		}
		mongoOperations.save(post);

		return "DONE, Potatoes tag in post : "+(post).toString();

	}
    
    
    public String postVote(String userId, String postId,boolean vote)throws InterruptedException, ExecutionException {
  		Query query = new Query();
  		query.addCriteria(Criteria.where("_id").is(postId));
  		Post post = mongoOperations.findById(query, Post.class, "post");
  		if(post==null){
  			return "post id is not valid";
  		}
  		boolean found =false;
  		for(PostVote pv: post.getPostVotes() ) {
  			if(pv.getVoterId().equals(userId)) {
  				found =true;
  				if(pv.isUpVote()==vote) {
  					post.delPostVote(pv);
  				}
  				else {
  					pv.setUpVote(vote);
  	
  				}
  				
  			}
  		}
  		if(!found) {
  			PostVote pv=new PostVote(userId,vote);
  			post.addPostVote(pv);
  		}
  		mongoOperations.save(post);

  		return "DONE, Potatoes tag in post : "+(post).toString();

  	}
	public String tagInPost(String postId, String[]userIds)throws InterruptedException, ExecutionException {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(postId));
		Post post = mongoOperations.findOne(query, Post.class, "post");
		if(post==null){
			return "post id is not valid";
		}
		for(String userId:userIds){
			//TODO: check if user id is valid-<<<<<<<<<<<<<<<<<<---
			post.addPostTags(userId);
		}
		mongoOperations.save(post);

		return "DONE, Potatoes tag in post : "+(post).toString();

	}

	public String delTagInPost(String postId, String[]userIds)throws InterruptedException, ExecutionException {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(postId));
		Post post = mongoOperations.findOne(query, Post.class, "post");
		if(post==null){
			return "post id is not valid";
		}
		for(String userId:userIds){
			//TODO: check if user id is valid-<<<<<<<<<<<<<<<<<<---
			post.delPostTags(userId);
		}
		mongoOperations.save(post);

		return "DONE, Potatoes tag in post : "+(post).toString();

	}

	public String commentTagInPost(String postId, String commentId, String[]userIds)throws InterruptedException, ExecutionException {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(postId));
		Post post = mongoOperations.findOne(query, Post.class, "post");
		if(post==null){
			return "post id is not valid";
		}
		Comment cmnt = post.getComments().get(0);//TODO: search for comment by commentId
		for(String userId:userIds){
			//TODO: check if user id is valid-<<<<<<<<<<<<<<<<<<---
			cmnt.addCommentTags(userId);
		}
		mongoOperations.save(post);

		return "DONE, Potatoes tag in post : "+(post).toString();

	}

	public String delCommentTagInPost(String postId, String commentId, String[]userIds)throws InterruptedException, ExecutionException {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(postId));
		Post post = mongoOperations.findOne(query, Post.class, "post");
		if(post==null){
			return "post id is not valid";
		}
		Comment cmnt = post.getComments().get(0);//TODO: search for comment by commentId
		for(String userId:userIds){
			//TODO: check if user id is valid-<<<<<<<<<<<<<<<<<<---
			cmnt.delCommentTags(userId);
		}
		mongoOperations.save(post);

		return "DONE, Potatoes tag in post : "+(post).toString();

	}
	static char getAnother(char c){
		if(c>='a' && c<='z'){
			int x=c-'a';
			return (char)('A'+x);
		}
		if(c>='A' && c<='Z'){
			int x=c-'A';
			return (char)('a'+x);
		}
		return c;
	}
	public String searchPosts(String subContent)throws InterruptedException, ExecutionException {
		Query query = new Query();
		StringBuilder pattern= new StringBuilder(".*");//starts with anything
		String[]tokens=subContent.split(" ");
		for(String s:tokens){
			pattern.append("(?i)").append(s);//case-insensitive
			pattern.append(".*");//followed by anything
		}
		query.addCriteria(Criteria.where("content").regex(pattern.toString()));
		List<Post> post = mongoOperations.find(query, Post.class, "post");

		return "DONE, Potatoes report post : "+(post);

	}
	
	// TAG
	public String assignModerator(String postId, String userId)throws InterruptedException, ExecutionException {
		System.out.println("hi");
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(postId));
//		Post post = mongoOperations.findOne(query, Post.class, "post");
		
		Update update = new Update().set("moderatorId", userId);
		mongoOperations.updateFirst(query, update, Post.class);
		
		return "DONE, Potatoes report post : ";

	}
	
	public String checkPostReports(String postId, String userId)throws InterruptedException, ExecutionException, ResponseStatusException {
		
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(postId));
		Post post = mongoOperations.findOne(query, Post.class, "post");
		
		if(!post.getModeratorId().equals(userId)) 
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "access denied");

		return "DONE, Potatoes report post : "+(post.getPostReports());

	}
	
	
	
	
	public List<Post> postRecommend(String userId)  {
    	
       //from the userId , if follwed posts is empty , then we check top 10 most voted posted to add in list 
		// get the list of blocked users
		
		Query query = new Query();
		//query.addCriteria(Criteria.where("userId").`is`()).with(Sort.by(Sort.Direction.DESC, "sortField"));

	//	query.addCriteria(Criteria.where("_id").is(postId));
		List<Post> posts = mongoOperations.find(query, Post.class, "post");
        
        
        
      
      
        return posts;
    }
	
	
	
	public ResponseEntity addImage(String postId,String photoRef) throws Exception  {
    	
	      
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(postId));
		Post post = mongoOperations.findOne(query, Post.class, "post");
		if(post!=null)
		{
			Update update = new Update().set("photoRef", photoRef);
			try
			{
				mongoOperations.updateFirst(query, update, Post.class); 

			}
			catch (Exception e) 
			{
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request");
			}
			
		}
		else
		{
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found");
		}
		 
	        
	      
	      
		return new ResponseEntity<>("Done Successfully", 
				   HttpStatus.OK);
	    }
	
	
	
}