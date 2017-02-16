/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

import com.vgorcinschi.rimmanew.entities.Groups;
import java.time.Instant;
import static org.hamcrest.Matchers.is;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

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
    @Ignore
    public void saveAGroup() {
        Groups group = new Groups("vgorcinschi", "su");
        assertTrue(repo.addGroup(group));
    }

    @Test
    public void findAGroup() {
        Groups group = repo.getByGroupName("admin");
        assertNotNull(group);
    }

    @Test
    public void updateAGroup() {
        Groups group = repo.getByGroupName("admin");
        group.setModifiedDate(Instant.now());
        group.setModifiedBy("junit test");
        assertTrue(repo.updateGroups(group));
    }

    @Test
    public void getAllGroups() {
        assertThat("Just checking that the"
                + " returned list is not empty", repo.getAll().isEmpty(), is(false));
    }
}
