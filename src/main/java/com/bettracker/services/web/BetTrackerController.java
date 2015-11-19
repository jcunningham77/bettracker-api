package com.bettracker.services.web;

import com.bettracker.services.exception.ResourceNotFoundException;
import com.bettracker.services.model.BetDAO;
import com.bettracker.services.model.UserDAO;
import com.bettracker.services.rest.ErrorMessage;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("betTrackerController")
public class BetTrackerController
{
 private static Logger logger = Logger.getLogger(BetTrackerController.class);
  @Autowired
  private UserDAO userDao;
  @Autowired
  private BetDAO betDao;
  @Autowired
  private HttpServletRequest request;
  
  
  @RequestMapping(value={"rest/handle"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  @ApiOperation(value="Hello World", notes="Test Method")
  @ApiResponses({@io.swagger.annotations.ApiResponse(code=200, message="Hello World", response=String.class)})
  public ResponseEntity<String> handle()
  {
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.set("MyResponseHeader", "MyValue");
    return new ResponseEntity("Hello World", responseHeaders, HttpStatus.OK);
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
    
    return new ResponseEntity(restBet, responseHeaders, HttpStatus.OK);
  }
  
  @RequestMapping(value={"rest/bet/"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  @ApiOperation(value="CreateBet", notes="Accepts a POST method to create a bet")
  public ResponseEntity<Object> createBet(@ApiParam(value="ID of the Associated User Record", required=true) @RequestParam Long user_id, @ApiParam(value="Wager of the Bet Record", required=true) @RequestParam Integer wager, @ApiParam(value="Result of the Bet Record", required=true) @RequestParam Integer result, @ApiParam(value="HomeTeam of the Bet Record", required=true) @RequestParam String homeTeam, @ApiParam(value="AwayTeam of the Bet Record", required=true) @RequestParam String awayTeam)
    throws ResourceNotFoundException
  {
    HttpHeaders responseHeaders = new HttpHeaders();
    com.bettracker.services.model.Bet bet = null;
    com.bettracker.services.rest.Bet restBet = new com.bettracker.services.rest.Bet();
    com.bettracker.services.rest.User restUser = new com.bettracker.services.rest.User();
    try
    {
      com.bettracker.services.model.User user = (com.bettracker.services.model.User)this.userDao.findOne(user_id);
      if (user == null)
      {
        logger.error("user not found, throwing Resource Not Found exception");
        logger.info("user not found, throwing Resource Not Found exception");
        throw new ResourceNotFoundException("Resource Not Found - no user with user_id: " + user_id, "No user with id = " + user_id + " found in repository");
      }
      bet = new com.bettracker.services.model.Bet(user, wager, result, homeTeam, awayTeam);
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
    }
    catch (Exception ex)
    {
      ErrorMessage errorMessage = new ErrorMessage();
      errorMessage.setMessage(ex.getMessage());
      return new ResponseEntity(errorMessage, responseHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity(restBet, responseHeaders, HttpStatus.CREATED);
  }
  
  @RequestMapping(value={"rest/bet/{bet-id}"}, method={org.springframework.web.bind.annotation.RequestMethod.PUT})
  @ApiOperation(value="UpdateBet", notes="Accepts a PUT method to update a bet")
  public ResponseEntity<Object> updateBet(@ApiParam(value="Id of the Bet record to update", required=true) @PathVariable("bet-id") Long betId, @ApiParam(value="Wager of the Bet Record", required=true) @RequestParam Integer wager, @ApiParam(value="Result of the Bet Record", required=true) @RequestParam Integer result, @ApiParam(value="HomeTeam of the Bet Record", required=true) @RequestParam String homeTeam, @ApiParam(value="AwayTeam of the Bet Record", required=true) @RequestParam String awayTeam)
    throws Exception
  {
    HttpHeaders responseHeaders = new HttpHeaders();
    com.bettracker.services.model.Bet bet = null;
    com.bettracker.services.rest.Bet restBet = new com.bettracker.services.rest.Bet();
    
    bet = (com.bettracker.services.model.Bet)this.betDao.findOne(betId);
    if (bet == null) {
      throw new ResourceNotFoundException("Resource Not Found", "No bet with id = " + betId.toString() + " found in repository");
    }
    bet.setWager(wager);
    bet.setResult(result);
    bet.setAwayTeam(awayTeam);
    bet.setHomeTeam(homeTeam);
    this.betDao.save(bet);
    
    restBet.setId(bet.getBet_id());
    restBet.setAwayTeam(bet.getAwayTeam());
    restBet.setHomeTeam(bet.getHomeTeam());
    restBet.setResult(bet.getResult());
    restBet.setWager(bet.getWager());
    
    return new ResponseEntity(restBet, responseHeaders, HttpStatus.OK);
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
    
    return new ResponseEntity(restBet, responseHeaders, HttpStatus.NO_CONTENT);
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
    List<com.bettracker.services.rest.Bet> restBets = new ArrayList();
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
    
    return new ResponseEntity(restUser, responseHeaders, HttpStatus.OK);
  }
  
  @RequestMapping(value={"rest/user/"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  @ApiOperation(value="CreateUser", notes="Accepts a POST method to create a user")
  public ResponseEntity<Object> createUser(@ApiParam(value="Email Address of the User Record", required=true) @RequestParam String email, @ApiParam(value="Name of the User Record", required=true) @RequestParam String name)
    throws Exception
  {
    HttpHeaders responseHeaders = new HttpHeaders();
    com.bettracker.services.model.User user = null;
    
    user = new com.bettracker.services.model.User(email, name);
    this.userDao.save(user);
    com.bettracker.services.rest.User restUser = new com.bettracker.services.rest.User();
    restUser.setId(user.getUser_id());
    restUser.setEmail(user.getEmail());
    restUser.setName(user.getName());
    return new ResponseEntity(restUser, responseHeaders, HttpStatus.OK);
  }
  
  @RequestMapping(value={"rest/user/{user-id}"}, method={org.springframework.web.bind.annotation.RequestMethod.PUT})
  @ApiOperation(value="UpdateUser", notes="Accepts a PUT method to update a user")
  public ResponseEntity<Object> updateUser(@ApiParam(value="Id of the User record to update", required=true) @PathVariable("user-id") Long userId, @ApiParam(value="Email of the User Record", required=true) @RequestParam String email, @ApiParam(value="Name of the User Record", required=true) @RequestParam String name)
    throws Exception
  {
    HttpHeaders responseHeaders = new HttpHeaders();
    com.bettracker.services.model.User user = null;
    com.bettracker.services.rest.User restUser = new com.bettracker.services.rest.User();
    
    user = (com.bettracker.services.model.User)this.userDao.findOne(userId);
    if (user == null) {
      throw new ResourceNotFoundException("Resource Not Found", "No user with id = " + userId.toString() + " found in repository");
    }
    user.setEmail(email);
    user.setName(name);
    this.userDao.save(user);
    
    restUser.setId(user.getUser_id());
    restUser.setEmail(user.getEmail());
    restUser.setName(user.getName());
    
    return new ResponseEntity(restUser, responseHeaders, HttpStatus.OK);
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
    
    return new ResponseEntity(restUser, responseHeaders, HttpStatus.NO_CONTENT);
  }
  
  @RequestMapping(value={"rest/user/byEmail"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  @ApiOperation(value="GetUserByEmail", notes="Currently, the email address used to filter the user is specified via a query string parameter based on an known issue. (See GitHub Issue)")
  public ResponseEntity<Object> getUserByEmail(
		  @ApiParam(value="Email Address of the Requested User Record", required=true) @RequestParam String email)
  {
    HttpHeaders responseHeaders = new HttpHeaders();
    com.bettracker.services.model.User user = null;
    com.bettracker.services.rest.User restUser = new com.bettracker.services.rest.User();
    Enumeration headerNames = this.request.getHeaderNames();
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
      List<com.bettracker.services.rest.Bet> restBets = new ArrayList();
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
      return new ResponseEntity(errorMessage, responseHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    logger.info("about to return this restUser = " + restUser.toString() + "and this response header = " + responseHeaders);
    return new ResponseEntity(restUser, HttpStatus.OK);
  }
}
