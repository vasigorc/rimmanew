/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.entities;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author vgorcinschi
 */
@Entity
@Access(AccessType.PROPERTY)
@Table(name = "credential")
public class Credential extends MetaInfo implements Serializable {

    private String username;
    private String passwd;
    private Groups group;
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

    @ManyToOne
    public Groups getGroups() {
        return group;
    }

    public void setGroups(Groups group) {
        this.group = group;
        updateModified();
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
        updateModified();
    }

    public boolean isSuspended() {
        return suspended;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
        updateModified();
    }
}
