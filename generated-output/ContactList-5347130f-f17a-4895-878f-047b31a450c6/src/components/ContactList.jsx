```jsx
import React from 'react';
import ContactItem from './ContactItem';

function ContactList({ contacts, deleteContact }) {
  return (
    <ul className="contact-list">
      {contacts.map((contact) => (
        <ContactItem key={contact.id} contact={contact} deleteContact={deleteContact} />
      ))}
    </ul>
  );
}

export default ContactList;

```