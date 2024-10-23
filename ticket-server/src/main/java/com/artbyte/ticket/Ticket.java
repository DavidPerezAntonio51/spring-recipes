package com.artbyte.ticket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {

    private String title;
    private String description;
    private Instant creationAt;
}
