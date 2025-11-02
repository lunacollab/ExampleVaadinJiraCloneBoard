package com.example.application.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@PageTitle("Login | Jira Clone")
@Route("login")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    public LoginView() {
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        getStyle().set("background", "linear-gradient(135deg, #667eea 0%, #764ba2 100%)");
        
        createLoginForm();
    }
    
    private void createLoginForm() {
        Div loginCard = new Div();
        loginCard.getStyle()
                .set("background", "white")
                .set("border-radius", "12px")
                .set("box-shadow", "0 10px 40px rgba(0,0,0,0.1)")
                .set("padding", "2rem")
                .set("width", "400px");
        
        Image logo = new Image("https://images.unsplash.com/photo-1564865878688-9a244444042a?w=100&h=100&fit=crop", "Logo");
        logo.setWidth("80px");
        logo.setHeight("80px");
        logo.getStyle().set("margin", "0 auto 1.5rem auto");
        
        Div logoContainer = new Div(logo);
        logoContainer.getStyle().set("text-align", "center");
        
        H1 title = new H1("Jira Clone");
        title.getStyle()
                .set("text-align", "center")
                .set("margin", "0 0 1.5rem 0")
                .set("font-size", "2rem")
                .set("color", "#172b4d");
        
        H1 subtitle = new H1("Đăng nhập vào Kanban Board");
        subtitle.getStyle()
                .set("text-align", "center")
                .set("margin", "0 0 2rem 0")
                .set("font-size", "1.25rem")
                .set("font-weight", "normal")
                .set("color", "#6B778C");
        
        EmailField usernameField = new EmailField("Email");
        usernameField.setWidthFull();
        usernameField.setPlaceholder("admin@jira.com");
        
        PasswordField passwordField = new PasswordField("Mật khẩu");
        passwordField.setWidthFull();
        passwordField.setPlaceholder("Nhập mật khẩu");
        
        Button loginBtn = new Button("Đăng nhập");
        loginBtn.setWidthFull();
        loginBtn.getStyle()
                .set("background", "#0052CC")
                .set("color", "white")
                .set("margin-top", "1rem")
                .set("padding", "0.75rem");
        
        loginBtn.addClickListener(e -> {
            if (usernameField.getValue().equals("admin@jira.com") && passwordField.getValue().equals("admin123")) {
                getUI().ifPresent(ui -> ui.navigate("/kanban"));
            } else {
                Notification.show("Sai email hoặc mật khẩu!", 3000, Notification.Position.MIDDLE);
            }
        });
        
        Div infoBox = new Div();
        infoBox.getStyle()
                .set("background", "#f4f5f7")
                .set("border-radius", "8px")
                .set("padding", "1rem")
                .set("margin-top", "1.5rem");
        
        infoBox.setText("Demo: admin@jira.com / admin123");
        infoBox.getStyle()
                .set("text-align", "center")
                .set("color", "#6B778C")
                .set("font-size", "0.875rem");
        
        FormLayout form = new FormLayout();
        form.setWidthFull();
        form.add(usernameField, passwordField);
        
        loginCard.add(logoContainer, title, subtitle, form, loginBtn, infoBox);
        add(loginCard);
    }
    
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && 
            !auth.getName().equals("anonymousUser")) {
            event.forwardTo("/kanban");
        }
    }
}

