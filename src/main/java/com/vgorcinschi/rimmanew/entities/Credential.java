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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author vgorcinschi
 */
@Entity
@Access(AccessType.PROPERTY)
@Table(name = "credential", uniqueConstraints = {
		@UniqueConstraint(columnNames = "username")})
public class Credential extends MetaInfo implements Serializable {

    private String username;
    private String passwd;
    private Set<Groups> groups;
    private boolean blocked;
    private boolean suspended;

    public Credential() {
        super();
    }

    public Credential(String createdBy) {
        super(Instant.now(), createdBy);
    }

    public Credential(String createdBy, String username, String passwd) {
        this(createdBy);
        this.username = username;
        this.passwd = passwd;
    }

    @Id
    @Column(name="username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        updateModified();
    }

    @Basic
    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
        updateModified();
    }

    @ManyToMany(mappedBy = "credentials")
    @JoinTable(
            name = "credential_groups",
            joinColumns = @JoinColumn(name = "credential"),
            inverseJoinColumns = @JoinColumn(name = "group")
    )
    public Set<Groups> getGroups() {
        return groups;
    }

    public void setGroups(Set<Groups> groups) {
        this.groups = groups;
        updateModified();
    }

    public void addAGroup(Groups group) {
        groups.add(group);
        updateModified();
    }

    public void removeAGroup(Groups group) {
        groups.remove(group);
        updateModified();
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }
}
