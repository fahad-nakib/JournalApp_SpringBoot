package com.engineeringdigest.journal.service;

import com.engineeringdigest.journal.entry.JournalEntry;
import com.engineeringdigest.journal.entry.User;
import com.engineeringdigest.journal.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {
    @Autowired
    private JournalEntryRepository journalEntryRepository;
    @Autowired
    private UserService userService;
    @Transactional
    public void saveEntry(JournalEntry journalEntry, String userName){
        User user = userService.findByUserName(userName);
        JournalEntry save = journalEntryRepository.save(journalEntry);
        user.getJournalEntries().add(save);
        userService.saveNewEntry(user);
    }

    public void saveEntry(JournalEntry journalEntry){
        journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getAll(){
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id)
    {
        return journalEntryRepository.findById(id);
    }
    @Transactional
    public boolean deleteById(ObjectId myId, String userName){
        boolean deleted = false;
        try {
            User user = userService.findByUserName(userName);
            boolean removed = user.getJournalEntries().removeIf(x -> x.getId().equals(myId));
            if (removed){
                userService.saveNewEntry(user);
                journalEntryRepository.deleteById(myId);
            }
            deleted = true;
        }catch (Exception e){
            System.out.println(e);
            throw new RuntimeException("An error occurred while deleting the entry.");
        }
        return deleted;
    }
}
