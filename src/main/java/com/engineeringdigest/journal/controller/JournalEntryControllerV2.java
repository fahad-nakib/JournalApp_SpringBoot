package com.engineeringdigest.journal.controller;

import com.engineeringdigest.journal.entry.JournalEntry;
import com.engineeringdigest.journal.entry.User;
import com.engineeringdigest.journal.service.JournalEntryService;
import com.engineeringdigest.journal.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {

    //  Creating object of JournalEntryService using @Autowired
    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;
    //    Fetching all information from database
    @GetMapping("{userName}")
    public ResponseEntity<?> getAllJournalEntriesOfUser(@PathVariable String userName){
        User user = userService.findByUserName(userName);
        List<JournalEntry> all = user.getJournalEntries();
        if (all != null && !all.isEmpty()){
            return new ResponseEntity<>(all,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //  Adding new item to Database
//    @PostMapping
//    public JournalEntry createEntry(@RequestBody JournalEntry myEntry){
//        myEntry.setDate(LocalDateTime.now());
//        journalEntryService.saveEntry(myEntry);
//        return myEntry;
//    }

    @PostMapping("{userName}")
    public ResponseEntity<JournalEntry> createEntryOfUser(@RequestBody JournalEntry myEntry, @PathVariable String userName){
        try {
            myEntry.setDate(LocalDateTime.now());
            journalEntryService.saveEntry(myEntry,userName);
            return new ResponseEntity<>(myEntry,HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //  Fetching a single element form database by Id.
//    @GetMapping("/id/{myId}")
//    public JournalEntry getJournalEntryById(@PathVariable ObjectId myId){
//        return journalEntryService.findById(myId).orElse(null);
//    }

    @GetMapping("/id/{myId}")                                                // path variable '/id/3'  || request parameter '/id?name=fahad
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId myId){
        //return journalEntryService.findById(myId).orElse(null);
        Optional<JournalEntry> journalEntry = journalEntryService.findById(myId);
        if (journalEntry.isPresent()){
            return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Deleting a single element from database by Id.
//    @DeleteMapping("/id/{myId}")                                             // path variable '/id/3'  || request parameter '/id?name=fahad
//    public boolean deleteJournalEntryById(@PathVariable ObjectId myId){
//        journalEntryService.deleteById(myId);
//        return true;
//    }

    @DeleteMapping("/{userName}/id/{myId}")                                             // path variable '/id/3'  || request parameter '/id?name=fahad
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable String userName, @PathVariable ObjectId myId){
        journalEntryService.deleteById(myId, userName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


//    @PutMapping("/id/{myId}")
//    public JournalEntry updateJournalEntryById(@PathVariable ObjectId myId, @RequestBody JournalEntry newEntry){
//        JournalEntry old = journalEntryService.findById(myId).orElse(null);
//        if (old != null){
//            old.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().equals("") ? newEntry.getTitle() : old.getTitle());
//            old.setContent(newEntry.getContent() != null && !newEntry.getContent().equals("") ? newEntry.getContent(): old.getContent());
//        }
//        journalEntryService.saveEntry(old);
//        return old;
//    }
    @PutMapping("/{userName}/id/{myId}")
    public ResponseEntity<?> updateJournalEntryById(
            @PathVariable ObjectId myId,
            @PathVariable String userName,
            @RequestBody JournalEntry newEntry
    ){
        JournalEntry old = journalEntryService.findById(myId).orElse(null);
        if (old != null){
            old.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().equals("") ? newEntry.getTitle() : old.getTitle());
            old.setContent(newEntry.getContent() != null && !newEntry.getContent().equals("") ? newEntry.getContent(): old.getContent());
            journalEntryService.saveEntry(old);
            return new ResponseEntity<>(old,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
