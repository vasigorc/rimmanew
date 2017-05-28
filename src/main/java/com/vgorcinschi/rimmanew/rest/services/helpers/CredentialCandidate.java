package com.vgorcinschi.rimmanew.rest.services.helpers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import java.util.Objects;

/**
 *
 * @author vgorcinschi
 */
@JsonRootName(value = "credential_candidate")
@JsonPropertyOrder({"username", "password", "group", "blocked", "suspended", 
    "firstName", "lastName", "emailAddress", "updatedBy"})
public class CredentialCandidate {
    public String username, password, group,firstName, lastName, emailAddress,
            updatedBy;
    public boolean blocked, suspended;

    public CredentialCandidate() {
    }
    
    @JsonProperty
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonProperty
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonProperty
    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @JsonProperty
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonProperty
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonProperty
    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @JsonProperty
    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @JsonProperty
    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    @JsonProperty
    public boolean isSuspended() {
        return suspended;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.username);
        hash = 41 * hash + Objects.hashCode(this.password);
        hash = 41 * hash + Objects.hashCode(this.group);
        hash = 41 * hash + Objects.hashCode(this.firstName);
        hash = 41 * hash + Objects.hashCode(this.lastName);
        hash = 41 * hash + Objects.hashCode(this.emailAddress);
        hash = 41 * hash + Objects.hashCode(this.updatedBy);
        hash = 41 * hash + (this.blocked ? 1 : 0);
        hash = 41 * hash + (this.suspended ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CredentialCandidate other = (CredentialCandidate) obj;
        if (this.blocked != other.blocked) {
            return false;
        }
        if (this.suspended != other.suspended) {
            return false;
        }
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        if (!Objects.equals(this.password, other.password)) {
            return false;
        }
        if (!Objects.equals(this.group, other.group)) {
            return false;
        }
        if (!Objects.equals(this.firstName, other.firstName)) {
            return false;
        }
        if (!Objects.equals(this.lastName, other.lastName)) {
            return false;
        }
        if (!Objects.equals(this.emailAddress, other.emailAddress)) {
            return false;
        }
        if (!Objects.equals(this.updatedBy, other.updatedBy)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CredentialCandidate{" + "username=" + username + ", password=" + password + ", group=" + group + ", firstName=" + firstName + ", lastName=" + lastName + ", emailAddress=" + emailAddress + ", updatedBy=" + updatedBy + ", blocked=" + blocked + ", suspended=" + suspended + '}';
    }
    
    
}
