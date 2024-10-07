package ch.supsi.webapp.web.controller;

import ch.supsi.webapp.web.model.*;
//import ch.supsi.webapp.web.repository.CommentRepository;
import ch.supsi.webapp.web.repository.AttachmentRepository;
import ch.supsi.webapp.web.repository.CommentRepository;
import ch.supsi.webapp.web.service.StatusService;
import ch.supsi.webapp.web.service.TicketService;
import ch.supsi.webapp.web.service.TypeService;
import ch.supsi.webapp.web.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TicketController {
    public static final String CTRL_HOME = "/home";
    public static final String CTRL_TICKET = "/ticket";
    public static final String CTRL_NEW = "/new";
    private static final String CTRL_EDIT = "/edit";
    private static final String CTRL_DELETE = "/delete";
    private static final String CTRL_ATTACHMENT = "/attachment";
    private static final String CTRL_SEARCH = "/search";
    private static final String CTRL_BOARD = "/board";

    private static final String MODEL_MENU_ITEM = "menuItem";

    @Autowired private TicketService ticketService;
    @Autowired private StatusService statusService;
    @Autowired private UserService userService;
    @Autowired private TypeService typeService;

    @Autowired private AttachmentRepository attachmentRepository;

    @Autowired private CommentRepository commentRepository;

    @ModelAttribute
    public void addAttributes(HttpServletRequest request, Model model) {
        //model.addAttribute("servletPath", request.getServletPath());
        model.addAttribute("homeUrl", CTRL_HOME);
        model.addAttribute("createTicketUrl", CTRL_TICKET + CTRL_NEW);
        model.addAttribute("loginUrl", UserController.CTRL_LOGIN);
        model.addAttribute("logoutUrl", UserController.CTRL_LOGOUT);
        model.addAttribute("registerUrl", UserController.CTRL_REGISTER);
    }

    @GetMapping({"/", CTRL_HOME})
    public String homePage(Model model) {
        List<Ticket> tickets = ticketService.getAllTickets();

        model.addAttribute(MODEL_MENU_ITEM, MenuItem.HOME);

        model.addAttribute("allTickets", tickets);

        return "homepage";
    }

    @GetMapping(CTRL_TICKET + "/{id}")
    public String viewTicketDetails(Model model, @PathVariable("id") Long id) {
        Ticket ticket = ticketService.getTicket(id);

        model.addAttribute("ticket", ticket);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal()
                instanceof org.springframework.security.core.userdetails.User) {
            var userLogged = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

            if(userLogged != null) {
                User user = userService.getUser(userLogged.getUsername());

                if(user != null) {
                    model.addAttribute("watched", user.getWatchedTickets().contains(ticket));
                }
            }
        }

        return "ticket-details";
    }

    @GetMapping(CTRL_TICKET + CTRL_NEW)
    public String createNewTicketPage(Model model) {

        Ticket ticket = new Ticket();
        model.addAttribute("ticket", ticket);

        List<Type> types = typeService.getAllTypes();
        model.addAttribute("types", types);

        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);

        return "new-ticket-form";
    }

    @PostMapping(CTRL_TICKET +  CTRL_NEW)
    public String createNewTicket(@ModelAttribute("ticket") Ticket ticket,
                                  @RequestParam("file") MultipartFile file) throws IOException {
        if(file != null && !file.isEmpty()) {
            Attachment attachment = new Attachment();
            attachment.setName(file.getOriginalFilename());
            attachment.setData(file.getBytes());
            attachment.setContentType(file.getContentType());

            ticket.setAttachment(attachment);
        }

        String username = ((org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        ticket.setAuthor(userService.getUser(username));

        ticketService.addTicket(ticket);

        return "redirect:" + CTRL_HOME;
    }

    @GetMapping(CTRL_TICKET + "/{id}" + CTRL_EDIT)
    public String editTicketPage(Model model, @PathVariable("id") Long id) {
        Ticket ticket = ticketService.getTicket(id);

        model.addAttribute("ticket", ticket);

        List<Status> statuses = statusService.getAllStatuses();
        model.addAttribute("statuses", statuses);

        List<Type> types = typeService.getAllTypes();
        model.addAttribute("types", types);

        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);

        return "edit-ticket-form";
    }

    @PostMapping(CTRL_TICKET + "/{id}" + CTRL_EDIT)
    @Transactional
    public String editTicket(Ticket ticket,
                             @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        Ticket ticketInRepo = ticketService.getTicket(ticket.getId());

        if(file == null) {
            Attachment attachment = ticketInRepo.getAttachment();
            ticket.setAttachment(attachment);
        }
        else if(!file.isEmpty()) {
            //delete current ticket attachment
            Attachment currentAttachment = ticketInRepo.getAttachment();
            if(currentAttachment != null) {
                attachmentRepository.delete(currentAttachment);
            }

            //upload new attachment
            Attachment attachment = new Attachment();
            attachment.setName(file.getOriginalFilename());
            attachment.setData(file.getBytes());
            attachment.setContentType(file.getContentType());

            ticket.setAttachment(attachment);
        }

        List<Comment> ticketComments = ticketInRepo.getComments();

        if(ticketComments != null) {
            ticket.setComments(ticketComments);
        }

        ticketService.editTicket(ticket);

        return "redirect:" + CTRL_HOME;
    }

    @GetMapping(CTRL_TICKET + "/{id}" + CTRL_DELETE)
    public String deleteTicket(@PathVariable("id") Long id) {
        Ticket ticket = ticketService.getTicket(id);

        ticketService.removeTicket(ticket);

        return "redirect:" + CTRL_HOME;
    }

    @GetMapping(CTRL_TICKET + "/{id}" + CTRL_ATTACHMENT + "/{name}")
    public ResponseEntity<InputStreamResource> viewTicketAttachment(@PathVariable("id") Long id) {
        Ticket ticket = ticketService.getTicket(id);
        Attachment ticketAttachment = ticket.getAttachment();

        InputStream inputStream = new ByteArrayInputStream(ticketAttachment.getData());
        InputStreamResource resource = new InputStreamResource(inputStream);

        String contentDisposition = "inline;filename=" + ticketAttachment.getName();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .contentType(MediaType.parseMediaType(ticketAttachment.getContentType()))
                .body(resource);
    }

    @GetMapping(CTRL_TICKET + CTRL_SEARCH)
    public ResponseEntity<List<Ticket>> search(@RequestParam("q") String value) {
        List<Ticket> tickets = ticketService.getAllTickets();

        List<Ticket> foundTickets = tickets.stream()
                .filter(ticket -> ticket.getTitle().startsWith(value)
                                || ticket.getDescription().startsWith(value)
                                || ticket.getAuthor().getUsername().startsWith(value)
                                || ticket.getAuthor().getFirstName().startsWith(value)
                                || ticket.getAuthor().getLastName().startsWith(value))
                .toList();


        return new ResponseEntity<>(foundTickets, HttpStatus.FOUND);
    }

    @GetMapping(CTRL_BOARD)
    public String boardPage(Model model) {
        List<Ticket> tickets = ticketService.getAllTickets();

        model.addAttribute(MODEL_MENU_ITEM, MenuItem.BOARD);

        model.addAttribute("openTickets", tickets
                .stream()
                .filter(t -> t.getStatus().getId().equals(Status.OPEN_ID))
                .toList());

        model.addAttribute("inProgressTickets", tickets
                .stream()
                .filter(t -> t.getStatus().getId().equals(Status.IN_PROGRESS_ID))
                .toList());

        model.addAttribute("doneTickets", tickets
                .stream()
                .filter(t -> t.getStatus().getId().equals(Status.DONE_ID))
                .toList());

        model.addAttribute("closedTickets", tickets
                .stream()
                .filter(t -> t.getStatus().getId().equals(Status.CLOSED_ID))
                .toList());

        return "boardpage";
    }

    @GetMapping(CTRL_TICKET + "/{id}/details")
    public ResponseEntity<Ticket> ticketDetails(@PathVariable("id") Long id) {
        Ticket ticket = ticketService.getTicket(id);

        return new ResponseEntity<>(ticket, HttpStatus.OK);
    }

    @PostMapping(CTRL_TICKET + "/{id}" + "/totalHours")
    public String setTicketResolutionTotalHours(@PathVariable("id") Long id,
                                                @RequestParam("resolutionTotalHours")
                                                Double resolutionTotalHours) {

        Ticket ticket = ticketService.getTicket(id);
        ticket.setResolutionTotalHours(resolutionTotalHours);

        ticketService.editTicket(ticket);

        return "redirect:" + CTRL_BOARD;
    }

    @GetMapping("/watches")
    public String watchesPage(Model model) {

        model.addAttribute(MODEL_MENU_ITEM, MenuItem.WATCHES);

        String username = ((org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

        User user = userService.getUser(username);

        model.addAttribute("watchedTickets", user.getWatchedTickets());

        return "watchespage";
    }

    @PostMapping(CTRL_TICKET + "/{id}" + "/watch")
    public ResponseEntity<Void> watchTicket(@PathVariable("id") Long id) {
        Ticket ticket = ticketService.getTicket(id);

        String username = ((org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

        User user = userService.getUser(username);
        user.getWatchedTickets().add(ticket);

        userService.editUser(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(CTRL_TICKET + "/{id}" + "/new-comment")
    public ResponseEntity<Void> addCommentToTicket(@PathVariable("id") Long id,
                                                   @RequestBody Comment comment) {
        Ticket ticket = ticketService.getTicket(id);

        String text = comment.getText();
        if(text != null) {
            comment.setCreationDate(LocalDateTime.now());

            String username = ((org.springframework.security.core.userdetails.User)
                    SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            User user = userService.getUser(username);
            comment.setUser(user);

            comment.setReplies(new ArrayList<>());

            ticketService.addComment(comment, ticket);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(CTRL_TICKET + "/{id}" + "/comments")
    public ResponseEntity<List<Comment>> getAllTicketComments(@PathVariable("id") Long id) {
        Ticket ticket = ticketService.getTicket(id);

        List<Comment> comments = ticket.getComments();

        if(comments != null) {
            return new ResponseEntity<>(comments, HttpStatus.OK);
        }

        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
    }

    @PostMapping(CTRL_TICKET + "/{ticketId}" + "/comment/{commentId}/reply")
    public ResponseEntity<Void> replyToTicketComment(@PathVariable("ticketId") Long ticketId,
                                                     @PathVariable("commentId") Long commentId,
                                                     @RequestBody Comment commentReply) {
        Ticket ticket = ticketService.getTicket(ticketId);
        Comment comment = commentRepository.findById(commentId).get();

        String replyText = commentReply.getText();

        if(replyText != null) {
            commentReply.setCreationDate(LocalDateTime.now());

            String username = ((org.springframework.security.core.userdetails.User)
                    SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            User user = userService.getUser(username);
            commentReply.setUser(user);

            commentReply.setReplies(new ArrayList<>());

            List<Comment> commentReplies = comment.getReplies();
            commentReplies.add(commentReply);

            commentRepository.save(comment);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
