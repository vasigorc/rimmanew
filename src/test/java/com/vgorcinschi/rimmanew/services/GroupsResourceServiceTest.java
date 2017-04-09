package com.vgorcinschi.rimmanew.services;

import com.vgorcinschi.rimmanew.ejbs.GroupsRepository;
import com.vgorcinschi.rimmanew.ejbs.OCGroupsRepository;
import com.vgorcinschi.rimmanew.rest.services.GroupsResourceService;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author vgorcinschi
 */
public class GroupsResourceServiceTest {
    
    private final GroupsResourceService groupsResourceService;
    private GroupsRepository repository;
        
    public GroupsResourceServiceTest() {
        groupsResourceService = new GroupsResourceService();
        groupsResourceService.setGroupsRepository(new OCGroupsRepository());
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test(expected = BadRequestException.class)
    public void nullPassedAsMethodAttribute(){
        groupsResourceService.getGroup(null);
    }
    
    @Test(expected=NotFoundException.class)
    public void invalidGroupPassedIn(){
        groupsResourceService.getGroup("doesntExist");
    }
    
    @Test
    public void successfullResponse(){
        Response success = groupsResourceService.getGroup("admin");
        assertTrue(success.getStatus()==200);
    }
    
    @Test
    public void getAllGroupsTest(){
        Response success = groupsResourceService.getGroups(0, 2);
        assertTrue(success.getStatus() == 200);
        System.out.println(success.getEntity().toString());
    }
}
