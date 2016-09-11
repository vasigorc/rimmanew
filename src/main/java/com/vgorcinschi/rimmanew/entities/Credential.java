/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.entities;

import static com.vgorcinschi.rimmanew.util.SecurityPrompt.randomSalt;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author vgorcinschi
 */
@Entity
@Access(AccessType.PROPERTY)
@Table(name = "credential")
@NamedQueries({
    @NamedQuery(name = "findByGroup",
            query = "SELECT c FROM "
            + "Credential c WHERE LOWER(c.group.groupName) LIKE :group"),
    @NamedQuery(name = "findAllCredentials",
            query = "SELECT c FROM Credential c order by c.modifiedDate DESC"),
    @NamedQuery(name = "findActiveCredentials",
            query = "SELECT c FROM Credential c WHERE c.blocked = FALSE AND"
                    + " c.suspended = FALSE order by c.modifiedDate DESC")
})
public class Credential extends MetaInfo implements Serializable {

    private String username;
    private byte[] passwd;
    private byte[] salt;
    private Groups group;
    private boolean blocked;
    private boolean suspended;
    private String firstname, lastname;

    public Credential() {
        super();
        salt = randomSalt();
    }

    public Credential(String createdBy) {
        super(Instant.now(), createdBy);
        salt = randomSalt();
    }

    public Credential(String createdBy, String username) {
        this(createdBy);
        this.username = username;
    }

    @Id
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        updateModified();
    }

    @Column(name = "passwd")
    public byte[] getPasswd() {
        return passwd;
    }

    public void setPasswd(byte[] passwd) {
        this.passwd = passwd;
        updateModified();
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    public Groups getGroup() {
        return group;
    }

    public void setGroup(Groups group) {
        this.group = group;
        group.getCredentials().add(this);
        updateModified();
    }

    @Column(name = "is_blocked")
    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
        updateModified();
    }

    @Column(name = "is_suspended")
    public boolean isSuspended() {
        return suspended;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
        updateModified();
    }

    @Column(name = "firstname")
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
        updateModified();
    }

    @Column(name = "lastname")
    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
        updateModified();
    }

    @Column(name = "salt")
    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    @Override
    public String toString() {
        return "Credential{" + "username=" + username + ", group=" + group + ", blocked=" + blocked + ", suspended=" + suspended + ", firstname=" + firstname + ", lastname=" + lastname + '}';
    }    
}
