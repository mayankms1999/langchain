```jsx
import React from 'react';

function ContactItem({ contact, deleteContact }) {
  return (
    <li className="contact-item">
      <span className="contact-name">{contact.name}</span>
      <span className="contact-email">{contact.email}</span>
      <span className="contact-phone">{contact.phone}</span>
      <button className="delete-button" onClick={() => deleteContact(contact.id)}>
        Delete
      </button>
    </li>
  );
}

export default ContactItem;

```