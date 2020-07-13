package com.geeklabs.spiffyshow.utils

import android.util.Log
import java.io.UnsupportedEncodingException
import java.util.*
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.Session
import javax.mail.internet.AddressException
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class GMail {
    private val emailPort = "587" // gmail's smtp port
    private val smtpAuth = "true"
    private val starttls = "true"
    private val emailHost = "smtp.gmail.com"
    private var fromEmail: String? = null
    private var fromPassword: String? = null
    private var toEmailList: List<String>? = null
    private var emailSubject: String? = null
    private var emailBody: String? = null
    private var emailProperties: Properties? = null
    private var mailSession: Session? = null
    private var emailMessage: MimeMessage? = null

    constructor()
    constructor(
        fromEmail: String?,
        fromPassword: String?,
        toEmailList: List<String>?,
        emailSubject: String?,
        emailBody: String?
    ) {
        this.fromEmail = fromEmail
        this.fromPassword = fromPassword
        this.toEmailList = toEmailList
        this.emailSubject = emailSubject
        this.emailBody = emailBody
        emailProperties = System.getProperties()
        emailProperties!!["mail.smtp.port"] = emailPort
        emailProperties!!["mail.smtp.auth"] = smtpAuth
        emailProperties!!["mail.smtp.starttls.enable"] = starttls
        Log.i("GMail", "Mail server properties set.")
    }

    @Throws(
        AddressException::class,
        MessagingException::class,
        UnsupportedEncodingException::class
    )
    fun createEmailMessage() {
        mailSession = Session.getDefaultInstance(emailProperties, null)
        emailMessage = MimeMessage(mailSession)
        emailMessage!!.setFrom(InternetAddress(fromEmail, fromEmail))
        for (toEmail in toEmailList!!) {
            Log.i("GMail", "toEmail: $toEmail")
            emailMessage!!.addRecipient(
                Message.RecipientType.TO,
                InternetAddress(toEmail)
            )
        }
        emailMessage!!.subject = emailSubject
        //        emailMessage.setContent(emailBody, "text/html");// for a html email
        emailMessage!!.setText(emailBody) // for a text email
        Log.i("GMail", "Email Message created.")
    }

    @Throws(AddressException::class, MessagingException::class)
    fun sendEmail() {
        val transport = mailSession!!.getTransport("smtp")
        transport.connect(emailHost, fromEmail, fromPassword)
        Log.i("GMail", "allrecipients: " + emailMessage!!.allRecipients)
        transport.sendMessage(emailMessage, emailMessage!!.allRecipients)
        transport.close()
        Log.i("GMail", "Email sent successfully.")
    }
}