package ch.supsi.webapp.web.controller;

import ch.supsi.webapp.web.model.Success;
import ch.supsi.webapp.web.model.Ticket;
import ch.supsi.webapp.web.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class TicketRestController {
    /*
    @Autowired
    private TicketService ticketService;


    @RequestMapping(value = "/tickets", method = RequestMethod.POST)
    public ResponseEntity<Ticket> create(@RequestBody Ticket ticket) {
        return new ResponseEntity<>(ticketService.addTicket(ticket), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/tickets", method = RequestMethod.GET)
    public ResponseEntity<List<Ticket>> list() {
        return new ResponseEntity<>(ticketService.getAllTickets(), HttpStatus.OK);
    }

    @RequestMapping(value = "/tickets/{id}", method = RequestMethod.GET)
    public ResponseEntity<Ticket> read(@PathVariable("id") Long id) {
        Ticket ticket = ticketService.getTicket(id);

        if(ticket != null) {
            return new ResponseEntity<>(ticket, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/tickets/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Ticket> edit(@PathVariable("id") Long id,
                                               @RequestBody Ticket ticket) {
        Ticket ticketEdited = ticketService.editTicket(id, ticket);

        if(ticketEdited != null) {
            return new ResponseEntity<>(ticketEdited, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/tickets/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Success> remove(@PathVariable("id") Long id) {
        Ticket ticketRemoved = ticketService.removeTicket(id);

        if(ticketRemoved != null) {
            return new ResponseEntity<>(Success.builder().success(true).build(), HttpStatus.OK);
        }

        return new ResponseEntity<>(Success.builder().success(false).build(), HttpStatus.NOT_FOUND);
    }

     */
}
