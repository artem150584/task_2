package org.example.web.controllers;

import org.apache.log4j.Logger;
import org.example.app.services.BookService;
import org.example.app.services.FileService;
import org.example.web.config.BookValidator;
import org.example.web.dto.Book;
import org.example.web.dto.BookIdToRemove;
import org.example.web.dto.BookPattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Paths;

@Controller
@RequestMapping(value = "books")
@Scope("singleton")
public class BookShelfController {

    public static final String EXTERNAL_UPLOADS_FOLDER = "external_uploads";
    private final String PROPERTY_FOR_SERVER_ROOT = "catalina.home";

    private Logger logger = Logger.getLogger(BookShelfController.class);
    private BookService bookService;
    private FileService fileService;
    // <PROJECT>\target\simple_mvc\WEB-INF\classes\files
    private final String DIRECTORY = Paths.get(getClass().getClassLoader().getResource("/files").toURI())
            .toFile().getAbsolutePath();


    @Autowired
    public BookShelfController(BookService bookService, FileService fileService) throws URISyntaxException {
        this.bookService = bookService;
        this.fileService = fileService;
    }

    @Autowired
    private ServletContext servletContext;

    @GetMapping("/shelf")
    public String books(Model model) {
        model.addAttribute("book", new Book());

        logger.info(this.toString());
        putBookClassicParam(model);

        return "book_shelf";
    }

    @PostMapping("/save")
    public String saveBook(@Valid Book book, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", book);
            putBookClassicParam(model);

            return "book_shelf";
        } else {
            bookService.saveBook(book);
            logger.info("Current repository contents: " + bookService.getAllBooks().size());
            return "redirect:/books/shelf";
        }
    }

    @PostMapping("/remove")
    public String removeBookBiId(@Valid BookIdToRemove bookIdToRemove, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", new Book());
            model.addAttribute("bookPatternToRemove", new BookPattern());
            model.addAttribute("bookPatternToFilter", new BookPattern());
            model.addAttribute("bookList", bookService.getAllBooks());
            model.addAttribute("fileToDownloadList", fileService.getAllFiles(DIRECTORY));

            return "book_shelf";
        } else {
            bookService.removeBookById(bookIdToRemove.getId());

            return "redirect:/books/shelf";
        }
    }

    @PostMapping("/remove_pattern")
    public String removeBookByPattern(@ModelAttribute("bookPatternToRemove") @Valid BookPattern bookPatternToRemove,
                                      BindingResult bindingResult,
                                      Model model) {
        BookValidator bookValidator = new BookValidator();
        bookValidator.validate(bookPatternToRemove, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("book", new Book());
            model.addAttribute("bookIdToRemove", new BookIdToRemove());
            model.addAttribute("bookPatternToFilter", new BookPattern());
            model.addAttribute("bookList", bookService.getAllBooks());
            model.addAttribute("fileToDownloadList", fileService.getAllFiles(DIRECTORY));

            return "book_shelf";
        } else {
            bookService.removeBookByPattern(bookPatternToRemove);

            return "redirect:/books/shelf";
        }
    }

    @PostMapping("/filter")
    public String filterBook(@ModelAttribute("bookPatternToFilter") @Valid BookPattern bookPatternToFilter,
                             BindingResult bindingResult,
                             Model model) {
        BookValidator bookValidator = new BookValidator();
        bookValidator.validate(bookPatternToFilter, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("bookList", bookService.getAllBooks());
        } else {
            model.addAttribute("bookPatternToFilter", new BookPattern());
            model.addAttribute("bookList", bookService.getFilteredBooks(bookPatternToFilter));

            logger.info("got book shelf with filter: " + bookPatternToFilter);
        }
        model.addAttribute("book", new Book());
        model.addAttribute("bookIdToRemove", new BookIdToRemove());
        model.addAttribute("bookPatternToRemove", new BookPattern());
        model.addAttribute("fileToDownloadList", fileService.getAllFiles(DIRECTORY));

        return "book_shelf";
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        String name = file.getOriginalFilename();
        byte[] bytes = file.getBytes();

        //create dir
        String rootPath = System.getProperty(PROPERTY_FOR_SERVER_ROOT);
        File dir = new File(rootPath + File.separator + EXTERNAL_UPLOADS_FOLDER);
        if (dir.exists()) {
            dir.mkdir();
        }

        //create file
        File serverFile = new File(dir.getAbsolutePath() + File.separator + name);
        try (BufferedOutputStream stream = new BufferedOutputStream((new FileOutputStream(serverFile)))) {
            stream.write(bytes);

            logger.info("new file saved at: " + serverFile.getAbsolutePath());
        } catch (IOException e) {
            logger.info("File was not downloaded");
        }

        return "redirect:/books/shelf";
    }

    @GetMapping("/download")
    public void downloadFile(HttpServletResponse resonse,
                             @RequestParam() String fileName) throws IOException {

        MediaType mediaType = MediaTypeUtils.getMediaTypeForFileName(this.servletContext, fileName);

        File file = new File(DIRECTORY + File.separator + fileName);

        resonse.setContentType(mediaType.getType());
        resonse.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName());
        resonse.setContentLength((int) file.length());

        BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(file));
        BufferedOutputStream outStream = new BufferedOutputStream(resonse.getOutputStream());

        byte[] buffer = new byte[1024];
        int bytesRead = 0;
        while ((bytesRead = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }
        outStream.flush();
        inStream.close();
    }

    private void putBookClassicParam(Model model) {
        model.addAttribute("bookIdToRemove", new BookIdToRemove());
        model.addAttribute("bookPatternToRemove", new BookPattern());
        model.addAttribute("bookPatternToFilter", new BookPattern());
        model.addAttribute("bookList", bookService.getAllBooks());
        model.addAttribute("fileToDownloadList", fileService.getAllFiles(DIRECTORY));
    }
}
