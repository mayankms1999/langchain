```jsx
import React, { useState, useEffect } from 'react';
import ContactList from './components/ContactList';
import ContactForm from './components/ContactForm';
import './App.css';

function App() {
  const [contacts, setContacts] = useState([]);

  useEffect(() => {
    // Load contacts from local storage on initial load
    const storedContacts = localStorage.getItem('contacts');
    if (storedContacts) {
      setContacts(JSON.parse(storedContacts));
    }
  }, []);

  useEffect(() => {
    // Save contacts to local storage whenever the contacts state changes
    localStorage.setItem('contacts', JSON.stringify(contacts));
  }, [contacts]);

  const addContact = (newContact) => {
    const contactToAdd = { ...newContact, id: Date.now().toString() };
    setContacts([...contacts, contactToAdd]);
  };

  const deleteContact = (id) => {
    setContacts(contacts.filter((contact) => contact.id !== id));
  };

  return (
    <div className="app-container">
      <h1>Contact List</h1>
      <ContactForm onAddContact={addContact} />
      <ContactList contacts={contacts} onDeleteContact={deleteContact} />
    </div>
  );
}

export default App;
```