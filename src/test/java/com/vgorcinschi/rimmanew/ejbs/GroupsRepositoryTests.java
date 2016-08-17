/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

import com.vgorcinschi.rimmanew.entities.Groups;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author vgorcinschi
 */
public class GroupsRepositoryTests {
    
    private final OCGroupsRepository repo;
    
    public GroupsRepositoryTests() {
        repo = new OCGroupsRepository();
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void saveAGroup(){
        Groups group = new Groups("vgorcinschi", "admin");
        assertTrue(repo.addGroup(group));
    }
}
