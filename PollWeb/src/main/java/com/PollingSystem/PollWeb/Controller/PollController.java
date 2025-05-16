package com.PollingSystem.PollWeb.Controller;

import com.PollingSystem.PollWeb.Entity.Poll;
import com.PollingSystem.PollWeb.repo.PollRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/polls")
@CrossOrigin(origins = "*")  // Allow all origins for CORS
public class PollController {

    @Autowired
    private PollRepo pollRepo;

    // Create new poll
    @PostMapping("/create")
    public ResponseEntity<?> createPoll(@RequestBody Poll poll) {
        try {
            // Initialize votes list with zeros (same size as options)
            List<Integer> votes = new ArrayList<>();
            for (int i = 0; i < poll.getOptions().size(); i++) {
                votes.add(0);
            }
            poll.setVotes(votes);

            Poll savedPoll = pollRepo.save(poll);
            return ResponseEntity.ok(savedPoll);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error creating poll: " + e.getMessage());
        }
    }

    // Get latest poll dynamically(after i post one)
    @GetMapping("/current")
    public ResponseEntity<?> getCurrentPoll() {
        Pageable topOne = PageRequest.of(0, 1);
        List<Poll> polls = pollRepo.findLatestPoll(topOne);

        if (polls.isEmpty()) {
            return ResponseEntity.status(404).body("No polls found");
        } else {
            return ResponseEntity.ok(polls.get(0));
        }
    }

    // Vote for option by index (optionIndex sent as query param)
    @PostMapping("/vote")
    public ResponseEntity<?> vote(@RequestParam int optionIndex) {
        Pageable topOne = PageRequest.of(0, 1);
        List<Poll> polls = pollRepo.findLatestPoll(topOne);

        if (polls.isEmpty()) {
            return ResponseEntity.status(404).body("No polls found");
        }

        Poll poll = polls.get(0);
        List<Integer> votes = poll.getVotes();

        if (optionIndex < 0 || optionIndex >= votes.size()) {
            return ResponseEntity.badRequest().body("Invalid option index");
        }

        votes.set(optionIndex, votes.get(optionIndex) + 1);
        poll.setVotes(votes);
        pollRepo.save(poll);

        return ResponseEntity.ok(poll);
    }
}