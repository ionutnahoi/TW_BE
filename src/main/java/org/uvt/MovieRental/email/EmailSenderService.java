package org.uvt.MovieRental.email;

import org.uvt.MovieRental.entity.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String toEmail, Movie movie) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("my.gmail@gmail.com");
        message.setTo(toEmail);
        message.setSubject(movie.getInfo().getTitle() + " is now available");
        String text = movie.getInfo().getTitle() + " by " + movie.getInfo().getGenre() + " is now available and can be rent on: http://localhost:3000/";
        message.setText(text);

        mailSender.send(message);
    }
}
