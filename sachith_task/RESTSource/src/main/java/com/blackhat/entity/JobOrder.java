package com.blackhat.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

/**
 *
 * @author Sachith Dickwella
 */
@NamedQueries({
    @NamedQuery(name = "getAllJobOrders", query = "from JobOrder j"),
    @NamedQuery(name = "getJobOrderById", query = "from JobOrder j where j.id = :id")})
@Entity
@Table(name = "JobOrder", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"ID"}),
    @UniqueConstraint(columnNames = {"REFERENCE_NO"})})
public class JobOrder implements Serializable {

    private static final long serialVersionUID = -7519241415237227977L;

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "ID")
    private Customer customerId;

    @NotNull
    @Column(name = "REFERENCE_NO", length = 75)
    private String referenceNumber;

    @NotNull
    @Column(name = "CREATE_USER", length = 30)
    private String createUser;

    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "CREATE_DATETIME")
    private Date createDateTime;

    @NotNull
    @Column(name = "LASTUPDATE_USER", length = 30)
    private String lastUpdateUser;

    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "LASTUPDATE_DATETIME")
    private Date lastUpdateDateTime;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "orderId")
    private List<SampleJob> sampleJobs;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the customerId
     */
    public Customer getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(Customer customerId) {
        this.customerId = customerId;
    }

    /**
     * @return the referenceNumber
     */
    public String getReferenceNumber() {
        return referenceNumber;
    }

    /**
     * @param referenceNumber the referenceNumber to set
     */
    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    /**
     * @return the createUser
     */
    public String getCreateUser() {
        return createUser;
    }

    /**
     * @param createUser the createUser to set
     */
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    /**
     * @return the createDateTime
     */
    public Date getCreateDateTime() {
        return createDateTime;
    }

    /**
     * @param createDateTime the createDateTime to set
     */
    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
    }

    /**
     * @return the lastUpdateUser
     */
    public String getLastUpdateUser() {
        return lastUpdateUser;
    }

    /**
     * @param lastUpdateUser the lastUpdateUser to set
     */
    public void setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }

    /**
     * @return the lastUpdateDateTime
     */
    public Date getLastUpdateDateTime() {
        return lastUpdateDateTime;
    }

    /**
     * @param lastUpdateDateTime the lastUpdateDateTime to set
     */
    public void setLastUpdateDateTime(Date lastUpdateDateTime) {
        this.lastUpdateDateTime = lastUpdateDateTime;
    }

    /**
     * @return the sampleJobs
     */
    public List<SampleJob> getSampleJobs() {
        return sampleJobs;
    }

    /**
     * @param sampleJobs the sampleJobs to set
     */
    public void setSampleJobs(List<SampleJob> sampleJobs) {
        this.sampleJobs = sampleJobs;
    }
}
