/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.entities;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author vgorcinschi
 */
@Entity
@Access(AccessType.PROPERTY)
@Table(name = "groups", uniqueConstraints = {
    @UniqueConstraint(columnNames = "group_name")})
public class Groups extends MetaInfo implements Serializable {

    private String groupName;
    private Set<Credential> credentials;

    public Groups() {
        super();
    }

    public Groups(String groupName) {
        super(Instant.now(), null);
        this.groupName = groupName;
    }

    public Groups(String createdBy, String groupName) {
        setCreatedBy(createdBy);
        this.groupName = groupName;
    }

    @Id
    @Column(name = "group_name")
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
        updateModified();
    }

    @ManyToMany
    public Set<Credential> getCredentials() {
        return credentials;
    }

    public void setCredentials(Set<Credential> credentials) {
        this.credentials = credentials;
    }
}
