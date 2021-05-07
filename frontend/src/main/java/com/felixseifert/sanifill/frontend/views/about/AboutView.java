/*
 * Copyright (C) 2021 Felix Seifert <mail@felix-seifert.com> (https://felix-seifert.com)
 *
 * This programme is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or any
 * later version.
 *
 * This programme is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.felixseifert.sanifill.frontend.views.about;

import com.felixseifert.sanifill.frontend.views.main.MainView;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "about", layout = MainView.class)
@PageTitle("About")
@CssImport("./views/about/about-view.css")
public class AboutView extends VerticalLayout {

    public AboutView() {
        addClassName("about-view");

        add(new HorizontalLayout(new Span("Sanifill displays the data of sensors. These sensors provide " +
                "information about the filling of the monitored liquidity containers (e.g. soap dispensers or hand " +
                "sanitiser bottles).")));
        add(new HorizontalLayout(new Span(new Span("The source code and a brief description can be found on GitHub: "),
                new Anchor("https://github.com/felix-seifert/Sanifill", "https://github.com/felix-seifert/Sanifill"))));
    }
}
