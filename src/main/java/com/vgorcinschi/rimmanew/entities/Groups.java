/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author vgorcinschi
 */
@Entity
@Access(AccessType.PROPERTY)
@Table(name = "groups")
@NamedQueries({
    @NamedQuery(name = "findAllGroups",
            query = "SELECT g FROM Groups g order by g.modifiedDate DESC")
})
public class Groups extends MetaInfo implements Serializable {
    
    private String groupName;
    private Set<Credential> credentials;
    
    public Groups() {
        super();
        credentials = new HashSet<>();
    }
    
    public Groups(String groupName) {
        this();
        this.groupName = groupName;
    }
    
    public Groups(String createdBy, String groupName) {
        this();
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
    
    @OneToMany
    @JoinTable(
            name = "credential_groups",
            joinColumns = @JoinColumn(name = "group_name"),
            inverseJoinColumns = @JoinColumn(name = "username", unique=true)
    )
    public Set<Credential> getCredentials() {
        return credentials;
    }
    
    public void setCredentials(Set<Credential> credentials) {
        this.credentials = credentials;
    }
    
    public void addCredential(Credential c) {
        credentials.add(c);
        if (c.getGroup() != this) {
            c.setGroup(this);
        }
    }
}
