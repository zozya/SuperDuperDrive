package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.form.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.form.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.service.FileService;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;

import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {

    private static final String MODEL_ATTRIBUTE_NOTE_FORM = "noteForm";
    private static final String MODEL_ATTRIBUTE_CREDENTIALS_FORM = "credentialForm";
    private static final String MODEL_ATTRIBUTE_FILES = "files";
    private static final String MODEL_ATTRIBUTE_NOTES = "notes";
    private static final String MODEL_ATTRIBUTE_CREDENTIAL = "credentials";

    @Autowired
    private FileService fileService;

    @Autowired
    private NoteService noteService;

    @Autowired
    private UserService userService;

    @Autowired
    private CredentialService credentialService;

    @GetMapping()
    public String homeView(@ModelAttribute(MODEL_ATTRIBUTE_NOTE_FORM) NoteForm noteForm,
                           @ModelAttribute(MODEL_ATTRIBUTE_CREDENTIALS_FORM) CredentialForm credentialForm, Principal principal, Model model) {

        User user = userService.getUser(principal.getName());

        List<File> files = fileService.getFiles(user.getUserid());
        List<Note> notes = noteService.getNotes(user.getUserid());
        List<Credential> credentials = credentialService.getCredentials(user.getUserid());

        model.addAttribute(MODEL_ATTRIBUTE_FILES, files);
        model.addAttribute(MODEL_ATTRIBUTE_NOTES, notes);
        model.addAttribute(MODEL_ATTRIBUTE_CREDENTIAL, credentials);

        return "home";
    }

    @PostMapping("/fileUpload")
    public String uploadFile(@RequestParam("fileUpload") MultipartFile file, Principal principal, RedirectAttributes redirectAttributes) {

        User user = userService.getUser(principal.getName());

        if(fileService.isFilenameExists(file.getOriginalFilename(), user.getUserid())) {
            redirectAttributes.addFlashAttribute("changeError", "The  [" + file.getOriginalFilename() + "] already exists.");
            return "redirect:/result";
        }

        try {
            fileService.createFile(file.getOriginalFilename(), file.getContentType(), file.getSize(), user.getUserid(), file.getBytes());
        } catch (IOException ioe) {
            redirectAttributes.addFlashAttribute("changeError", "The filedate couldn't be converted.");
            return "redirect:/result";
        }
        catch (SQLException sqle) {
            redirectAttributes.addFlashAttribute("changeError", "The file couldn't be saved.");
            return "redirect:/result";
        }

        redirectAttributes.addFlashAttribute("changeSuccess", true);
        return "redirect:/result";
    }

    @GetMapping("/fileView")
    public ResponseEntity<byte[]> viewFile(@RequestParam("filename") String filename, Principal principal) {
        User user = userService.getUser(principal.getName());

        File file = fileService.getFile(filename, user.getUserid());
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());

        String boundary = MimeTypeUtils.generateMultipartBoundaryString();
        MediaType multipartType = MediaType.parseMediaType(file.getContenttype() + ";boundary=" + boundary);
        headers.setContentType(multipartType);

        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(file.getFiledata(), headers, HttpStatus.OK);
        return responseEntity;
    }

    @GetMapping("/fileDelete")
    public String deleteFile(@RequestParam("filename") String filename, Principal principal, RedirectAttributes redirectAttributes) {
        User user = userService.getUser(principal.getName());

        try {
            fileService.deleteFile(filename, user.getUserid());
        } catch(Exception e) {
            redirectAttributes.addFlashAttribute("changeError", "The file couldn't be deleted.");
            return "redirect:/result";
        }

        redirectAttributes.addFlashAttribute("changeSuccess", true);
        return "redirect:/result";
    }

    @PostMapping("/noteSaveOrEdit")
    public String saveOrEditNote(@ModelAttribute(MODEL_ATTRIBUTE_NOTE_FORM) NoteForm noteForm, RedirectAttributes redirectAttributes, Principal principal) {

        User user = userService.getUser(principal.getName());
        try {
            noteService.saveOrUpdateNote(user.getUserid(), noteForm.getNoteId(), noteForm.getNoteTitle(), noteForm.getNoteDescription());
        } catch(Exception e) {
            redirectAttributes.addFlashAttribute("changeError", "The note couldn't be saved.");
            return "redirect:/result";
        }
        redirectAttributes.addFlashAttribute("changeSuccess", true);
        return "redirect:/result";
    }

    @GetMapping("/noteDelete")
    public String deleteNote(@RequestParam("noteid") int noteid, Principal principal, RedirectAttributes redirectAttributes) {
        User user = userService.getUser(principal.getName());

        try {
            noteService.deleteNote(noteid, user.getUserid());
        } catch(Exception e) {
            redirectAttributes.addFlashAttribute("changeError", "The note couldn't be deleted.");
            return "redirect:/result";
        }

        redirectAttributes.addFlashAttribute("changeSuccess", true);
        return "redirect:/result";
    }

    @PostMapping("/credentialSaveOrEdit")
    public String saveOrEditCredential(@ModelAttribute(MODEL_ATTRIBUTE_CREDENTIALS_FORM) CredentialForm credentialForm, RedirectAttributes redirectAttributes, Principal principal) {

        User user = userService.getUser(principal.getName());
        try {
            credentialService.saveCredentialOrEdit(credentialForm.getUrl(), credentialForm.getUsername(), credentialForm.getPassword(), credentialForm.getCredentialId(), user.getUserid());
        } catch(Exception e) {
            redirectAttributes.addFlashAttribute("changeError", "The credential couldn't be saved.");
            return "redirect:/result";
        }
        redirectAttributes.addFlashAttribute("changeSuccess", true);
        return "redirect:/result";
    }

    @GetMapping("/credentialDelete")
    public String deleteCredential(@RequestParam("credentialid") int credentialid, Principal principal, RedirectAttributes redirectAttributes) {
        User user = userService.getUser(principal.getName());

        try {
            credentialService.deleteCredential(credentialid, user.getUserid());
        } catch(Exception e) {
            redirectAttributes.addFlashAttribute("changeError", "The credential couldn't be deleted.");
            return "redirect:/result";
        }

        redirectAttributes.addFlashAttribute("changeSuccess", true);
        return "redirect:/result";
    }

}
