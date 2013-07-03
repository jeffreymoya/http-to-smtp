
package com.ge.convert.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "translate", namespace = "http://convert.ge.com/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "translate", namespace = "http://convert.ge.com/", propOrder = {
    "sender",
    "recipient",
    "cc",
    "bcc",
    "subject",
    "body",
    "sourceSystemId",
    "sourceSystemPwd"
})
public class Translate {

    @XmlElement(name = "sender", namespace = "")
    private String sender;
    @XmlElement(name = "recipient", namespace = "")
    private String recipient;
    @XmlElement(name = "cc", namespace = "")
    private String cc;
    @XmlElement(name = "bcc", namespace = "")
    private String bcc;
    @XmlElement(name = "subject", namespace = "")
    private String subject;
    @XmlElement(name = "body", namespace = "")
    private String body;
    @XmlElement(name = "sourceSystemId", namespace = "")
    private String sourceSystemId;
    @XmlElement(name = "sourceSystemPwd", namespace = "")
    private String sourceSystemPwd;

    /**
     * 
     * @return
     *     returns String
     */
    public String getSender() {
        return this.sender;
    }

    /**
     * 
     * @param sender
     *     the value for the sender property
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getRecipient() {
        return this.recipient;
    }

    /**
     * 
     * @param recipient
     *     the value for the recipient property
     */
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getCc() {
        return this.cc;
    }

    /**
     * 
     * @param cc
     *     the value for the cc property
     */
    public void setCc(String cc) {
        this.cc = cc;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getBcc() {
        return this.bcc;
    }

    /**
     * 
     * @param bcc
     *     the value for the bcc property
     */
    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getSubject() {
        return this.subject;
    }

    /**
     * 
     * @param subject
     *     the value for the subject property
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getBody() {
        return this.body;
    }

    /**
     * 
     * @param body
     *     the value for the body property
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getSourceSystemId() {
        return this.sourceSystemId;
    }

    /**
     * 
     * @param sourceSystemId
     *     the value for the sourceSystemId property
     */
    public void setSourceSystemId(String sourceSystemId) {
        this.sourceSystemId = sourceSystemId;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getSourceSystemPwd() {
        return this.sourceSystemPwd;
    }

    /**
     * 
     * @param sourceSystemPwd
     *     the value for the sourceSystemPwd property
     */
    public void setSourceSystemPwd(String sourceSystemPwd) {
        this.sourceSystemPwd = sourceSystemPwd;
    }

}
