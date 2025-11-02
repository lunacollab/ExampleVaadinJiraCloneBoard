package com.example.application.jira.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;

public class MainLayout extends AppLayout {

    public MainLayout() {
        setPrimarySection(Section.NAVBAR);
        addToNavbar(createHeader());
    }

    private Div createHeader() {
        Image logo = new Image("https://images.unsplash.com/photo-1564865878688-9a244444042a?w=100&h=100&fit=crop", "Logo");
        logo.setWidth("40px");
        logo.setHeight("40px");
        logo.addClassName("cursor-pointer");
        logo.addClickListener(e -> UI.getCurrent().navigate("/kanban"));
        
        Span appName = new Span("Jira Clone");
        appName.getStyle().set("font-size", "1.25rem").set("font-weight", "600");
        appName.addClassName("cursor-pointer");
        appName.addClickListener(e -> UI.getCurrent().navigate("/kanban"));
        
        Button logoutBtn = new Button("Đăng xuất", VaadinIcon.SIGN_OUT.create());
        logoutBtn.addThemeVariants(com.vaadin.flow.component.button.ButtonVariant.LUMO_ERROR);
        logoutBtn.addClickListener(e -> UI.getCurrent().getPage().setLocation("/login"));
        
        Div logoSection = new Div(logo, appName);
        logoSection.getStyle()
                .set("display", "flex")
                .set("align-items", "center")
                .set("gap", "1rem");
        
        Div header = new Div(logoSection, logoutBtn);
        header.getStyle()
                .set("display", "flex")
                .set("justify-content", "space-between")
                .set("align-items", "center")
                .set("padding", "0 1.5rem")
                .set("height", "64px")
                .set("width", "100%")
                .set("border-bottom", "1px solid #e5e7eb");
        
        return header;
    }
}

