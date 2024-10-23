package com.artbyte.ticket;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequestScoped
@Path("/api/ticket")
public class TicketResource {

    List<Ticket> ticketList = new ArrayList<>();

    public TicketResource(){
        ticketList.add(new Ticket("Ticket_1", "Ticket de muestra 1", Instant.now()));
        ticketList.add(new Ticket("Ticket_2", "Ticket de muestra 2", Instant.now()));
        ticketList.add(new Ticket("Ticket_3", "Ticket de muestra 3", Instant.now()));
        ticketList.add(new Ticket("Ticket_4", "Ticket de muestra 4", Instant.now()));
        ticketList.add(new Ticket("Ticket_5", "Ticket de muestra 5", Instant.now()));
    }

    @GET
    @RolesAllowed({"USER", "ADMIN"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTickets(){
        return Response.ok(ticketList).build();
    }

    @GET
    @Path("/{title}")
    @RolesAllowed({"USER", "ADMIN"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTicketByTitle(@PathParam("title") String title) {
        Optional<Ticket> ticketOpt = this.ticketList.stream()
                .filter(t -> t.getTitle().equals(title))
                .findFirst();
        if (ticketOpt.isPresent()) {
            return Response.ok(ticketOpt.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Ticket not found with title: " + title)
                    .build();
        }
    }

    @POST
    @RolesAllowed("ADMIN")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTicket(Ticket ticket){
        this.ticketList.add(ticket);
        return Response.ok().build();
    }

}
