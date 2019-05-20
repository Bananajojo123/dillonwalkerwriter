package com.writer.dillon;

import com.backendless.Backendless;
import com.backendless.exceptions.BackendlessFault;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class User {
    private List<Book> booksBought;
    Dictionary progressInBook = new Hashtable();
    String email;
    private String objectId;

    public User() {

    }

    public User(List<Book> booksBought, Dictionary progressInBook, String email) {
        this.booksBought = booksBought;
        this.progressInBook = progressInBook;
        this.email = email;
    }

    public User(String email) {
        this.email = email;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public void saveUser()
    {
        Contact contact = new Contact();
        contact.setName( "Jack Daniels" );
        contact.setAge( 147 );
        contact.setPhone( "777-777-777" );
        contact.setTitle( "Favorites" );

        // save object synchronously
        Contact savedContact = Backendless.Persistence.save( contact );

        // save object asynchronously
        Backendless.Persistence.save( contact, new AsyncCallback<Contact>() {
            public void handleResponse( Contact response )
            {
                // new Contact instance has been saved
            }

            public void handleFault( BackendlessFault fault )
            {
                // an error has occurred, the error code can be retrieved with fault.getCode()
            }
        });
    }
}
