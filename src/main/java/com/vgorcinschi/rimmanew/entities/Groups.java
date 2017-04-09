package com.vgorcinschi.rimmanew.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author vgorcinschi
 */
@JsonRootName(value="userGroup")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Access(AccessType.PROPERTY)
@Table(name = "groups")
@NamedQueries({
    @NamedQuery(name = "findAllGroups",
            query = "SELECT g FROM Groups g order by g.modifiedDate DESC")
})
public class Groups extends MetaInfo implements Serializable {

    private String groupName;
    private Set<Credential> credentials = new HashSet<>();           

    public Groups() {
        super();
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
//    @JsonGetter("group_Name")
    @JsonValue
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
        updateModified();
    }

    @OneToMany(fetch=FetchType.EAGER, mappedBy = "group")
    @JsonIgnore
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

    @Override
    public String toString() {
        return "Groups{" + "groupName=" + groupName + '}';
    }
}