package com.bettracker.services.web;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bettracker.services.exception.InvalidRequestBodyException;
import com.bettracker.services.exception.ResourceNotFoundException;
import com.bettracker.services.model.BetDAO;
import com.bettracker.services.model.UserDAO;
import com.bettracker.services.model.BetQueryDAO;
import com.bettracker.services.rest.Bet;
import com.bettracker.services.rest.ErrorMessage;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;

@Controller("betTrackerController")
public class BetTrackerController
{
 private static Logger logger = Logger.getLogger(BetTrackerController.class);
  @Autowired
  private UserDAO userDao;
  @Autowired
  private BetDAO betDao;
  @Autowired
  private BetQueryDAO betQueryDao;  
  @Autowired
  private HttpServletRequest request;
  
  
  @RequestMapping(value={"rest/handle"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  @ApiOperation(value="Hello World", notes="Test Method")
  @ApiResponses({@io.swagger.annotations.ApiResponse(code=200, message="Hello World", response=String.class)})
  public ResponseEntity<String> handle()
  {
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.set("MyResponseHeader", "MyValue");
    return new ResponseEntity<String>("Hello World", responseHeaders, HttpStatus.OK);
  }
  
  @RequestMapping(value={"rest/bet/{bet-id}"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  @ApiOperation(value="GetBet", notes="Accepts a GET method to retrieve a bet by bet ID")
  public ResponseEntity<Object> getBet(@ApiParam(value="UniqueKey for Bet", required=true) @PathVariable("bet-id") Long betId)
    throws Exception
  {
    logger.info("entering");
    HttpHeaders responseHeaders = new HttpHeaders();
    com.bettracker.services.model.Bet bet = new com.bettracker.services.model.Bet();
    com.bettracker.services.rest.Bet restBet = new com.bettracker.services.rest.Bet();
    com.bettracker.services.rest.User restUser = new com.bettracker.services.rest.User();
    
    bet = (com.bettracker.services.model.Bet)this.betDao.findOne(betId);
    if (bet == null) {
      throw new ResourceNotFoundException("Resource Not Found", "No bet with id = " + betId.toString() + " found in repository");
    }
    com.bettracker.services.model.User user = bet.getUser();
    restBet.setId(bet.getBet_id());
    restBet.setAwayTeam(bet.getAwayTeam());
    restBet.setHomeTeam(bet.getHomeTeam());
    restBet.setResult(bet.getResult());
    restBet.setWager(bet.getWager());
    restUser.setEmail(user.getEmail());
    restUser.setName(user.getName());
    restUser.setId(user.getUser_id());
    restBet.setUser(restUser);
    
    return new ResponseEntity<Object>(restBet, responseHeaders, HttpStatus.OK);
  }
  //this is pretty redundant to the getuser method, just wanted to try to get access the the data in a slightly different way
  @RequestMapping(value={"rest/bet/user/{user-id}"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  @ApiOperation(value="GetBetsByUserId", notes="Accepts a GET method to retrieve a bet by user ID")
  public ResponseEntity<Object> getBetsByUserId(@ApiParam(value="UniqueKey for User", required=true) @PathVariable("user-id") Long userId)
    throws Exception
  {
    logger.info("entering");
    HttpHeaders responseHeaders = new HttpHeaders();
    com.bettracker.services.rest.Bet restBet = new com.bettracker.services.rest.Bet();
    com.bettracker.services.model.User user = this.userDao.findOne(userId);
    
    List<com.bettracker.services.model.Bet> bets = this.betQueryDao.findByUser(user);
    if (bets == null) {
      logger.info("no bets found");
      throw new ResourceNotFoundException("Resource Not Found", "No bet with user_id = " + userId.toString() + " found in repository");
    }
    List<com.bettracker.services.rest.Bet> restBets = new ArrayList<com.bettracker.services.rest.Bet>();
    com.bettracker.services.model.Bet bet;
    Iterator<com.bettracker.services.model.Bet> betsIterator = bets.iterator();
    while(betsIterator.hasNext()){
    	bet = (com.bettracker.services.model.Bet)betsIterator.next();
    	restBet.setId(bet.getBet_id());
        restBet.setAwayTeam(bet.getAwayTeam());
        restBet.setHomeTeam(bet.getHomeTeam());
        restBet.setResult(bet.getResult());
        restBet.setWager(bet.getWager());
    	restBets.add(restBet);
    }
    

    
    
    return new ResponseEntity<Object>(restBets, responseHeaders, HttpStatus.OK);
  }  
  
  
  
  @RequestMapping(value={"rest/bet"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  @ApiOperation(value="CreateBet", notes="Accepts a POST method to create a bet")
  public ResponseEntity<Object> createBet(@ApiParam(value="RequestBody with a JSON RestBet", required=true) @RequestBody com.bettracker.services.rest.Bet inputRestBet)
    throws Exception
  {
    HttpHeaders responseHeaders = new HttpHeaders();
    com.bettracker.services.model.Bet bet = null;
    com.bettracker.services.rest.Bet restBet = new com.bettracker.services.rest.Bet();
    com.bettracker.services.rest.User restUser = new com.bettracker.services.rest.User();
    try
    {
      com.bettracker.services.model.User user = (com.bettracker.services.model.User)this.userDao.findOne(inputRestBet.getUser().getId());
      if (user == null)
      {
        logger.error("user not found, throwing Resource Not Found exception");
        logger.info("user not found, throwing Resource Not Found exception");
        throw new ResourceNotFoundException("Resource Not Found - no user with user_id: " + inputRestBet.getUser().getId(), "No user with id = " + inputRestBet.getUser().getId() + " found in repository");
      }
      bet = new com.bettracker.services.model.Bet(user, inputRestBet.getWager(), inputRestBet.getResult(), inputRestBet.getHomeTeam(), inputRestBet.getAwayTeam());
      
      
      logger.info("About to save bet:" + bet.toString());
      this.betDao.save(bet);
      
      restBet.setId(bet.getBet_id());
      restBet.setAwayTeam(bet.getAwayTeam());
      restBet.setHomeTeam(bet.getHomeTeam());
      restBet.setResult(bet.getResult());
      restBet.setWager(bet.getWager());
      restUser.setEmail(user.getEmail());
      restUser.setName(user.getName());
      restUser.setId(user.getUser_id());
      restBet.setUser(restUser);
    } catch (ConstraintViolationException ex) {
    	throw new InvalidRequestBodyException("Required Field not set", "Required field not set in the request"); 
 	} catch (InvalidRequestBodyException ex) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage(ex.getMessage());
        return new ResponseEntity<Object>(errorMessage, responseHeaders, HttpStatus.BAD_REQUEST);	    
  	} catch (Exception ex) {
      ErrorMessage errorMessage = new ErrorMessage();
      errorMessage.setMessage(ex.getMessage());
      return new ResponseEntity<Object>(errorMessage, responseHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<Object>(restBet, responseHeaders, HttpStatus.CREATED);
  }
  
  @RequestMapping(value={"rest/bet"}, method={org.springframework.web.bind.annotation.RequestMethod.PUT})
  @ApiOperation(value="UpdateBet", notes="Accepts a PUT method to update a bet")
  public ResponseEntity<Object> updateBet(@ApiParam(value="RequestBody with a JSON com.bettracker.services.rest.Bet", required=true) @RequestBody com.bettracker.services.rest.Bet inputRestBet)
  {
    HttpHeaders responseHeaders = new HttpHeaders();
    try{
	    com.bettracker.services.model.Bet bet = null;
	    com.bettracker.services.rest.Bet restBet = new com.bettracker.services.rest.Bet();
	    
	    bet = (com.bettracker.services.model.Bet)this.betDao.findOne(inputRestBet.getId());
	    if (bet == null) {
	      throw new ResourceNotFoundException("Resource Not Found", "No bet with id = " + inputRestBet.getId() + " found in repository");
	    }
	    //manually throw a InvalidRequestBodyException if one of the @NotNull Bet fields is missing - 
	    //for some reason this isn't resulting in an ConstraintViolationException like it does on the
	    //insert
	    if (inputRestBet.getWager()==null){
	    	throw new InvalidRequestBodyException("Required Field, \"wager\", not set", "Required field, \"wager\", not set in the request");
	    }
	    
	    if (inputRestBet.getHomeTeam()==null){
	    	throw new InvalidRequestBodyException("Required Field,  \"homeTeam\",  not set", "Required field, \"homeTeam\", not set in the request");
	    }	  
	    
	    if (inputRestBet.getAwayTeam()==null){
	    	throw new InvalidRequestBodyException("Required Field, \"awayTeam\", not set", "Required field, \"awayTeam\", not set in the request");
	    }		    
	    
	    bet.setWager(inputRestBet.getWager());
	    bet.setResult(inputRestBet.getResult());
	    bet.setAwayTeam(inputRestBet.getAwayTeam());
	    bet.setHomeTeam(inputRestBet.getHomeTeam());
	    logger.info("About to save bet:" + bet.toString());
	    this.betDao.save(bet);
	    
	    restBet.setId(bet.getBet_id());
	    restBet.setAwayTeam(bet.getAwayTeam());
	    restBet.setHomeTeam(bet.getHomeTeam());
	    restBet.setResult(bet.getResult());
	    restBet.setWager(bet.getWager());
	    
	    return new ResponseEntity<Object>(restBet, responseHeaders, HttpStatus.OK);
	//TODO: move the below exception handling to @ControllerAdvice    
    } catch (ConstraintViolationException ex) {
        throw new InvalidRequestBodyException("Required field not set on request", "Required field not set on request body"); 	    
  	} catch (InvalidRequestBodyException ex) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage(ex.getMessage());
        return new ResponseEntity<Object>(errorMessage, responseHeaders, HttpStatus.BAD_REQUEST);	    
  	} catch (Exception ex) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage(ex.getMessage());
        return new ResponseEntity<Object>(errorMessage, responseHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
    }   
  }  
  
  @RequestMapping(value={"rest/bet/{bet-id}"}, method={org.springframework.web.bind.annotation.RequestMethod.DELETE})
  @ApiOperation(value="DeleteBet", notes="Accepts a DELETE method to delete a bet")
  public ResponseEntity<Object> deleteBet(@ApiParam(value="Id of the Bet record to update", required=true) @PathVariable("bet-id") Long betId)
    throws Exception
  {
    HttpHeaders responseHeaders = new HttpHeaders();
    com.bettracker.services.model.Bet bet = null;
    com.bettracker.services.rest.Bet restBet = new com.bettracker.services.rest.Bet();
    
    bet = (com.bettracker.services.model.Bet)this.betDao.findOne(betId);
    if (bet == null) {
      throw new ResourceNotFoundException("Resource Not Found", "No bet with id = " + betId.toString() + " found in repository");
    }
    this.betDao.delete(bet);
    
    return new ResponseEntity<Object>(restBet, responseHeaders, HttpStatus.NO_CONTENT);
  }
  
  @RequestMapping(value={"rest/user/{user-id}"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  @ApiOperation(value="GetUser", notes="Accepts a GET method to retrieve a user by user ID")
  public ResponseEntity<Object> getUser(@ApiParam(value="UniqueKey for User", required=true) @PathVariable("user-id") Long userId)
    throws Exception
  {
    logger.info("entering");
    HttpHeaders responseHeaders = new HttpHeaders();
    com.bettracker.services.model.User user = new com.bettracker.services.model.User();
    com.bettracker.services.rest.User restUser = new com.bettracker.services.rest.User();
    
    user = (com.bettracker.services.model.User)this.userDao.findOne(userId);
    if (user == null) {
      throw new ResourceNotFoundException("Resource Not Found", "No user with id = " + userId.toString() + " found in repository");
    }
    restUser.setId(user.getUser_id());
    restUser.setEmail(user.getEmail());
    restUser.setName(user.getName());
    
    Set<com.bettracker.services.model.Bet> bets = user.getBets();
    Iterator<com.bettracker.services.model.Bet> iter = bets.iterator();
    List<com.bettracker.services.rest.Bet> restBets = new ArrayList<Bet>();
    while (iter.hasNext())
    {
      com.bettracker.services.rest.Bet restBet = new com.bettracker.services.rest.Bet();
      com.bettracker.services.model.Bet bet = (com.bettracker.services.model.Bet)iter.next();
      logger.info("bet.bet_id " + bet.getBet_id());
      restBet.setAwayTeam(bet.getAwayTeam());
      restBet.setHomeTeam(bet.getHomeTeam());
      restBet.setId(bet.getBet_id());
      restBet.setWager(bet.getWager());
      restBet.setResult(bet.getResult());
      restBets.add(restBet);
    }
    restUser.setBets(restBets);
    
    return new ResponseEntity<Object>(restUser, responseHeaders, HttpStatus.OK);
  }
  
  @RequestMapping(value={"rest/user"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  @ApiOperation(value="CreateUser", notes="Accepts a POST method to create a user")
  public ResponseEntity<Object> createUser(@ApiParam(value="RequestBody with a JSON RestUser - ID as well as Bet fields will be ignored by this method, since this is for creating NEW user records", required=true, defaultValue="{\"email\": \"jcunningham77@gmail.com\",\"name\": \"Jeffrey B Cunningham\"}") @RequestBody com.bettracker.services.rest.User inputRestUser)
  throws Exception
  {
	HttpHeaders responseHeaders = new HttpHeaders();
	try {

	    com.bettracker.services.model.User user = null;
	    
	    user = new com.bettracker.services.model.User(inputRestUser.getEmail(), inputRestUser.getName());
	    this.userDao.save(user);
	    com.bettracker.services.rest.User restUser = new com.bettracker.services.rest.User();
	    restUser.setId(user.getUser_id());
	    restUser.setEmail(user.getEmail());
	    restUser.setName(user.getName());
	    return new ResponseEntity<Object>(restUser, responseHeaders, HttpStatus.OK);
	} catch (ConstraintViolationException ex) {
		logger.info("Caught ConstraintViolationException ex  =" + ex.getMessage());
        throw new InvalidRequestBodyException("Required field not set on request", "Required field not set on request body"); 	    
  	}catch (Exception ex) {
      ErrorMessage errorMessage = new ErrorMessage();
      errorMessage.setMessage(ex.getMessage());
      return new ResponseEntity<Object>(errorMessage, responseHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
  }
  
  @RequestMapping(value={"rest/user"}, method={org.springframework.web.bind.annotation.RequestMethod.PUT})
  @ApiOperation(value="UpdateUser", notes="Accepts a PUT method to update a user")
  public ResponseEntity<Object> updateUser(@ApiParam(value="RequestBody with a JSON RestUser", required=true, defaultValue="{\"id\": 1,\"email\": \"jcunningham77@gmail.com\",\"name\": \"Jeffrey B Cunningham\"}") @RequestBody com.bettracker.services.rest.User inputRestUser)
    throws ResourceNotFoundException, InvalidRequestBodyException, Exception
  {
    HttpHeaders responseHeaders = new HttpHeaders();
    com.bettracker.services.model.User user = null;
    com.bettracker.services.rest.User restUser = new com.bettracker.services.rest.User();
    
    user = (com.bettracker.services.model.User)this.userDao.findOne(inputRestUser.getId());
    if (user == null) {
      throw new ResourceNotFoundException("Resource Not Found", "No user with id = " + inputRestUser.getId() + " found in repository");
    }
    
    if (inputRestUser.getEmail()==null){
    	throw new InvalidRequestBodyException("Required Field, 'email', not set", "Required field, \"email\", not set in the request");
    }

    if (inputRestUser.getName()==null){
    	throw new InvalidRequestBodyException("Required Field, 'name', not set", "Required field, \"name\", not set in the request");
    }    
    
    
    
    user.setEmail(inputRestUser.getEmail());
    user.setName(inputRestUser.getName());
    this.userDao.save(user);
    
    restUser.setId(user.getUser_id());
    restUser.setEmail(user.getEmail());
    restUser.setName(user.getName());
    
    return new ResponseEntity<Object>(restUser, responseHeaders, HttpStatus.OK);
  }
  
  @RequestMapping(value={"rest/user/{user-id}"}, method={org.springframework.web.bind.annotation.RequestMethod.DELETE})
  @ApiOperation(value="DeleteUser", notes="Accepts a DELETE method to delete a user")
  public ResponseEntity<Object> deleteUser(@ApiParam(value="Id of the User record to update", required=true) @PathVariable("user-id") Long userId)
    throws Exception
  {
    HttpHeaders responseHeaders = new HttpHeaders();
    com.bettracker.services.model.User user = null;
    com.bettracker.services.rest.User restUser = new com.bettracker.services.rest.User();
    
    user = (com.bettracker.services.model.User)this.userDao.findOne(userId);
    if (user == null) {
      throw new ResourceNotFoundException("Resource Not Found", "No user with id = " + userId.toString() + " found in repository");
    }
    this.userDao.delete(user);
    
    return new ResponseEntity<Object>(restUser, responseHeaders, HttpStatus.NO_CONTENT);
  }
  
  @RequestMapping(value={"rest/user/byEmail"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  @ApiOperation(value="GetUserByEmail", notes="Currently, the email address used to filter the user is specified via a query string parameter based on an known issue. (See GitHub Issue)")
  public ResponseEntity<Object> getUserByEmail(
		  @ApiParam(value="Email Address of the Requested User Record", required=true) @RequestParam String email)
  {
    HttpHeaders responseHeaders = new HttpHeaders();
    com.bettracker.services.model.User user = null;
    com.bettracker.services.rest.User restUser = new com.bettracker.services.rest.User();
    Enumeration<?> headerNames = this.request.getHeaderNames();
    while (headerNames.hasMoreElements())
    {
      String key = (String)headerNames.nextElement();
      String value = this.request.getHeader(key);
      logger.info("http headers: key= " + key + ", value= " + value);
    }
    try
    {
      logger.info("get user by email = " + email);
      user = this.userDao.findByEmail(email);
      if (user==null){
    	  throw new ResourceNotFoundException("Resource Not Found", "No user with email = " + email.toString() + " found in repository");
      }
      logger.info("found following user = " + user.toString());
      
      restUser.setId(user.getUser_id());
      restUser.setEmail(user.getEmail());
      restUser.setName(user.getName());
      
      Set<com.bettracker.services.model.Bet> bets = user.getBets();
      Iterator<com.bettracker.services.model.Bet> iter = bets.iterator();
      List<com.bettracker.services.rest.Bet> restBets = new ArrayList<Bet>();
      while (iter.hasNext())
      {
        com.bettracker.services.rest.Bet restBet = new com.bettracker.services.rest.Bet();
        com.bettracker.services.model.Bet bet = (com.bettracker.services.model.Bet)iter.next();
        logger.info("bet.bet_id " + bet.getBet_id());
        restBet.setAwayTeam(bet.getAwayTeam());
        restBet.setHomeTeam(bet.getHomeTeam());
        restBet.setId(bet.getBet_id());
        restBet.setWager(bet.getWager());
        restBet.setResult(bet.getResult());
        restBets.add(restBet);
      }
      restUser.setBets(restBets);
      
      
    }
    catch (Exception ex)
    {
      ErrorMessage errorMessage = new ErrorMessage();
      errorMessage.setMessage(ex.getMessage());
      return new ResponseEntity<Object>(errorMessage, responseHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    logger.info("about to return this restUser = " + restUser.toString() + "and this response header = " + responseHeaders);
    return new ResponseEntity<Object>(restUser, HttpStatus.OK);
  }
}
