/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.entities;

import com.vgorcinschi.rimmanew.helpers.InstantConverter;
import java.time.Instant;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.MappedSuperclass;
/**
 *
 * @author vgorcinschi
 */
@MappedSuperclass
@Access(AccessType.PROPERTY)
public abstract class MetaInfo {

    private Instant createdDate, modifiedDate;
    private String createdBy, modifiedBy;

    public MetaInfo() {
        this.createdDate = Instant.now();
        this.modifiedDate = createdDate;
    }

    public MetaInfo(Instant createdDate, String createdBy) {
        this.createdDate = createdDate;
        this.modifiedDate = createdDate;
        this.createdBy = createdBy;
        this.modifiedBy = createdBy;
    }

    @Convert(converter = InstantConverter.class)
    @Column(name = "created_date")
    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createDate) {
        this.createdDate = createDate;
        if (modifiedDate == null) {
            modifiedDate = createdDate;
        }
    }

    @Convert(converter = InstantConverter.class)
    @Column(name = "modified_date")
    public Instant getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Instant modifyDate) {
        this.modifiedDate = modifyDate;
    }

    @Column(name = "created_by")
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        if (modifiedBy == null) {
            modifiedBy = createdBy;
        }
    }

    @Column(name = "modified_by")
    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public void updateModified() {
        setModifiedDate(Instant.now());
        if (getCreatedDate() == null) {
            setCreatedDate(getModifiedDate());
        }
    }

}
