package com.engineeringdigest.journal.entry;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
@Data  // includes @getter, @setter, etc
@NoArgsConstructor
public class User {
    @Id
    private ObjectId id;
    // this is for userName have to be unique, to achieve this need to configure application.properties.(auto-index-creation=true)
    @Indexed(unique = true)
    @NonNull
    private String userName;
    @NonNull                    // this is for password can not be null
    private String password;
    @DBRef                      // this is for, creating reference inside user collection, of JournalEntries  DB(journal_entries)
    private List<JournalEntry> journalEntries = new ArrayList<>();
}
