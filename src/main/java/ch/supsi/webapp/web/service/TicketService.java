package ch.supsi.webapp.web.service;

import ch.supsi.webapp.web.model.Comment;
import ch.supsi.webapp.web.model.Status;
import ch.supsi.webapp.web.model.Ticket;
import ch.supsi.webapp.web.repository.AttachmentRepository;
import ch.supsi.webapp.web.repository.StatusRepository;
import ch.supsi.webapp.web.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private AttachmentRepository attachmentRepository;


    public Ticket addTicket(Ticket ticket) {
        ticket.setCreationDate(LocalDateTime.now());
        ticket.setStatus(statusRepository.findById(Status.OPEN_ID).get());

        return ticketRepository.save(ticket);
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Ticket getTicket(Long id) {
        return ticketRepository.findById(id).orElse(null);
    }

    public Ticket editTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public Ticket removeTicket(Ticket ticket) {
        ticketRepository.delete(ticket);

        return ticket;
    }

    public void addComment(Comment comment, Ticket ticket) {
        if(ticket.getComments() == null) {
            ticket.setComments(new ArrayList<>());
        }

        ticket.getComments().add(comment);

        ticketRepository.save(ticket);
    }
}
