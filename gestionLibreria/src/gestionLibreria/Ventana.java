package gestionLibreria;

import gestionLibreria.extensiones.*;
import gestionLibreria.inventario.*;
import gestionLibreria.utilidades.GestorPersistencia;

import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

import java.time.LocalDate;
import java.util.*;

public class Ventana {

    private final Stage stage;
    private final Inventario inventario;
    private final GestorPersistencia gestor;

    public Ventana(Stage stage, Inventario inventario, GestorPersistencia gestor) {
        this.stage = stage;
        this.inventario = inventario;
        this.gestor = gestor;
    }

    public void iniciar() {
        stage.setTitle("Gestión de Librería");
        stage.setOnCloseRequest(e -> {
            e.consume();
            guardarYSalir();
        });
        stage.setScene(crearEscenaPrincipal());
        stage.show();
    }

    // ---------------------------------------------------------------
    // Escena principal
    // ---------------------------------------------------------------
    private Scene crearEscenaPrincipal() {
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));

        Label titulo = new Label("=== Gestión de Librería ===");
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button[] botones = {
            btn("1. Inventario",       e -> menuInventario()),
            btn("2. Ver Socios",       e -> menuSocios()),
            btn("3. Registrar Socio",  e -> registrarSocio()),
            btn("4. Vender Libro",     e -> venderLibro()),
            btn("5. Prestar Libro",    e -> prestarLibro()),
            btn("6. Buscar Libro",     e -> buscarLibro()),
            btn("7. Guardar y Salir",  e -> guardarYSalir())
        };
        for (Button b : botones) b.setMaxWidth(Double.MAX_VALUE);

        root.getChildren().add(titulo);
        root.getChildren().add(new Separator());
        root.getChildren().addAll(botones);

        return new Scene(root, 350, 420);
    }

    // ---------------------------------------------------------------
    // 1. Inventario
    // ---------------------------------------------------------------
    private void menuInventario() {
        ObservableList<Seccion> secciones = inventario.getSeccionesAsObservableList();
        if (secciones.isEmpty()) { info("No hay secciones en el inventario."); return; }

        Seccion seccion = elegirSeccion(secciones);
        if (seccion == null) return;

        ObservableList<String> llaves = seccion.GetLlaves();
        if (llaves.isEmpty()) { info("La sección '" + seccion.getNombre() + "' está vacía."); return; }

        Alert accion = new Alert(Alert.AlertType.CONFIRMATION);
        accion.setTitle("Inventario — " + seccion.getNombre());
        accion.setHeaderText("Libros en " + seccion.getNombre() + ":\n  " + String.join(", ", llaves));
        accion.setContentText("¿Qué desea hacer?");
        ButtonType btnVer    = new ButtonType("Ver Información");
        ButtonType btnAgregar = new ButtonType("Agregar Libro");
        accion.getButtonTypes().setAll(btnVer, btnAgregar, ButtonType.CANCEL);

        Optional<ButtonType> res = accion.showAndWait();
        if (!res.isPresent() || res.get() == ButtonType.CANCEL) return;
        if (res.get() == btnVer)     verInformacionLibro(seccion);
        else                         agregarLibroASeccion(seccion);
    }

    private Seccion elegirSeccion(ObservableList<Seccion> secciones) {
        Dialog<Seccion> dlg = new Dialog<>();
        dlg.setTitle("Secciones");
        dlg.setHeaderText("Seleccione una sección:");

        ListView<Seccion> lv = new ListView<>(secciones);
        lv.setCellFactory(l -> new ListCell<Seccion>() {
            @Override protected void updateItem(Seccion s, boolean empty) {
                super.updateItem(s, empty);
                setText(empty || s == null ? "" : s.getNombre());
            }
        });
        lv.getSelectionModel().selectFirst();
        lv.setPrefHeight(150);

        ButtonType okBtn = new ButtonType("Seleccionar", ButtonBar.ButtonData.OK_DONE);
        dlg.getDialogPane().getButtonTypes().addAll(okBtn, ButtonType.CANCEL);
        dlg.getDialogPane().setContent(lv);
        dlg.setResultConverter(bt -> bt == okBtn ? lv.getSelectionModel().getSelectedItem() : null);
        return dlg.showAndWait().orElse(null);
    }

    private void verInformacionLibro(Seccion seccion) {
        Optional<String> titOpt = pedirTexto("Ver Libro", "Título del libro:");
        if (!titOpt.isPresent() || titOpt.get().trim().isEmpty()) return;

        ObservableList<Libro> libros = seccion.encontrarLibrosPorTitulo(titOpt.get().trim());
        if (libros == null || libros.isEmpty()) { info("Libro no encontrado."); return; }

        StringBuilder sb = new StringBuilder();
        for (Libro l : libros) sb.append(libroToString(l)).append("\n---\n");
        mostrarTexto("Información del Libro", titOpt.get(), sb.toString());
    }

    private void agregarLibroASeccion(Seccion seccion) {
        Dialog<Libro> dlg = new Dialog<>();
        dlg.setTitle("Agregar Libro — " + seccion.getNombre());
        dlg.setHeaderText("Complete los datos del libro:");

        ButtonType okBtn = new ButtonType("Agregar", ButtonBar.ButtonData.OK_DONE);
        dlg.getDialogPane().getButtonTypes().addAll(okBtn, ButtonType.CANCEL);

        // campos
        ChoiceBox<String> cbTipo  = new ChoiceBox<>();
        cbTipo.getItems().addAll("normal", "arrendable", "digital");
        cbTipo.setValue("normal");

        TextField fTitulo    = new TextField();
        TextField fEdicion   = new TextField();
        TextField fCategoria = new TextField();
        TextField fPaginas   = new TextField();
        TextField fPrecio    = new TextField();
        TextField fFecha     = new TextField(LocalDate.now().toString());
        TextField fAutores   = new TextField();  // separados por ;
        TextField fMulta     = new TextField("0");
        TextField fMemoria   = new TextField("0");
        TextField fFormato   = new TextField("PDF");

        Label lMulta   = new Label("Multa:");
        Label lMemoria = new Label("Memoria (MB):");
        Label lFormato = new Label("Formato:");

        // visibilidad dinámica
        fMulta.setManaged(false);  fMulta.setVisible(false);
        lMulta.setManaged(false);  lMulta.setVisible(false);
        fMemoria.setManaged(false); fMemoria.setVisible(false);
        lMemoria.setManaged(false); lMemoria.setVisible(false);
        fFormato.setManaged(false); fFormato.setVisible(false);
        lFormato.setManaged(false); lFormato.setVisible(false);

        cbTipo.getSelectionModel().selectedItemProperty().addListener((obs, ov, nv) -> {
            boolean arr = "arrendable".equals(nv);
            boolean dig = "digital".equals(nv);
            fMulta.setManaged(arr);  fMulta.setVisible(arr);
            lMulta.setManaged(arr);  lMulta.setVisible(arr);
            fMemoria.setManaged(dig); fMemoria.setVisible(dig);
            lMemoria.setManaged(dig); lMemoria.setVisible(dig);
            fFormato.setManaged(dig); fFormato.setVisible(dig);
            lFormato.setManaged(dig); lFormato.setVisible(dig);
            dlg.getDialogPane().getScene().getWindow().sizeToScene();
        });

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(8);
        grid.setPadding(new Insets(15));
        int r = 0;
        grid.addRow(r++, new Label("Tipo:"),                   cbTipo);
        grid.addRow(r++, new Label("Título:"),                 fTitulo);
        grid.addRow(r++, new Label("Edición:"),                fEdicion);
        grid.addRow(r++, new Label("Categoría:"),              fCategoria);
        grid.addRow(r++, new Label("Páginas:"),                fPaginas);
        grid.addRow(r++, new Label("Precio:"),                 fPrecio);
        grid.addRow(r++, new Label("Fecha (yyyy-MM-dd):"),     fFecha);
        grid.addRow(r++, new Label("Autores (sep. por ;):"),   fAutores);
        grid.addRow(r++, lMulta,   fMulta);
        grid.addRow(r++, lMemoria, fMemoria);
        grid.addRow(r,   lFormato, fFormato);

        dlg.getDialogPane().setContent(grid);

        dlg.setResultConverter(bt -> {
            if (bt != okBtn) return null;
            try {
                LocalDate fecha   = LocalDate.parse(fFecha.getText().trim());
                String titulo     = fTitulo.getText().trim();
                String edicion    = fEdicion.getText().trim();
                String cat        = fCategoria.getText().trim();
                int pag           = Integer.parseInt(fPaginas.getText().trim());
                int precio        = Integer.parseInt(fPrecio.getText().trim());
                int id            = inventario.getNumeroLibros() + 1;
                ArrayList<String> autores = new ArrayList<>(
                        Arrays.asList(fAutores.getText().trim().split(";")));

                switch (cbTipo.getValue()) {
                    case "arrendable":
                        int multa = Integer.parseInt(fMulta.getText().trim());
                        return new LibroPrestable(fecha, titulo, edicion, cat,
                                                  pag, id, precio, autores, multa);
                    case "digital":
                        int memoria    = Integer.parseInt(fMemoria.getText().trim());
                        String formato = fFormato.getText().trim();
                        return new LibroDigital(fecha, titulo, edicion, cat,
                                                pag, id, precio, autores, memoria, formato);
                    default:
                        return new Libro(fecha, titulo, edicion, cat, pag, id, precio, autores);
                }
            } catch (Exception e) {
                error("Datos inválidos: " + e.getMessage());
                return null;
            }
        });

        dlg.showAndWait().ifPresent(libro -> {
            seccion.agregarLibro(libro);
            inventario.incrementarNumeroLibros();
            info("Libro agregado con éxito!");
        });
    }

    // ---------------------------------------------------------------
    // 2. Ver Socios
    // ---------------------------------------------------------------
    private void menuSocios() {
        ObservableList<Socio> socios = inventario.getSociosAsObservableList();
        if (socios.isEmpty()) { info("No hay socios registrados."); return; }

        Dialog<Socio> dlg = new Dialog<>();
        dlg.setTitle("Socios");
        dlg.setHeaderText("Seleccione un socio:");

        TableView<Socio> tabla = new TableView<>(socios);
        TableColumn<Socio, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(cd -> cd.getValue().nombreProperty());
        colNombre.setPrefWidth(180);
        TableColumn<Socio, String> colRut = new TableColumn<>("RUT");
        colRut.setCellValueFactory(cd -> cd.getValue().rutProperty());
        colRut.setPrefWidth(120);
        tabla.getColumns().addAll(colNombre, colRut);
        tabla.setPrefSize(320, 220);

        ButtonType okBtn = new ButtonType("Ver Detalles", ButtonBar.ButtonData.OK_DONE);
        dlg.getDialogPane().getButtonTypes().addAll(okBtn, ButtonType.CANCEL);
        dlg.getDialogPane().setContent(tabla);
        dlg.setResultConverter(bt -> bt == okBtn
                ? tabla.getSelectionModel().getSelectedItem() : null);

        dlg.showAndWait().ifPresent(s -> {
            StringBuilder sb = new StringBuilder();
            sb.append("Nombre:   ").append(s.getNombre()).append("\n");
            sb.append("RUT:      ").append(s.getRut()).append("\n");
            sb.append("Contacto: ").append(s.getNumeroContacto()).append("\n\n");
            sb.append("Libros prestados:\n");
            if (s.getLibrosPrestados().isEmpty()) {
                sb.append("  (ninguno)");
            } else {
                for (Libro l : s.getLibrosPrestados()) {
                    sb.append("  - ").append(l.getTitulo())
                      .append("  (ID: ").append(l.getIdInterno()).append(")\n");
                }
            }
            mostrarTexto("Detalle del Socio", s.getNombre(), sb.toString());
        });
    }

    // ---------------------------------------------------------------
    // 3. Registrar Socio
    // ---------------------------------------------------------------
    private void registrarSocio() {
        Dialog<Socio> dlg = new Dialog<>();
        dlg.setTitle("Registrar Socio");
        dlg.setHeaderText("Ingrese los datos del nuevo socio:");

        ButtonType okBtn = new ButtonType("Registrar", ButtonBar.ButtonData.OK_DONE);
        dlg.getDialogPane().getButtonTypes().addAll(okBtn, ButtonType.CANCEL);

        TextField fNombre = new TextField();
        TextField fRut    = new TextField();
        TextField fNum    = new TextField();

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(8);
        grid.setPadding(new Insets(15));
        grid.addRow(0, new Label("Nombre:"),                fNombre);
        grid.addRow(1, new Label("RUT (xxxxxxxx-x):"),      fRut);
        grid.addRow(2, new Label("Teléfono (+569xxxxxxxx):"), fNum);
        dlg.getDialogPane().setContent(grid);

        dlg.setResultConverter(bt -> {
            if (bt != okBtn) return null;
            String n = fNombre.getText().trim();
            String r = fRut.getText().trim();
            String t = fNum.getText().trim();
            if (n.isEmpty() || r.isEmpty() || t.isEmpty()) {
                error("Todos los campos son obligatorios.");
                return null;
            }
            return new Socio(n, r, t);
        });

        dlg.showAndWait().ifPresent(s -> {
            inventario.setSocio(s.getRut(), s);
            info("Socio '" + s.getNombre() + "' registrado con éxito!");
        });
    }

    // ---------------------------------------------------------------
    // 4. Vender Libro
    // ---------------------------------------------------------------
    private void venderLibro() {
        Optional<String> nombreOpt = pedirTexto("Vender Libro", "Nombre del libro:");
        if (!nombreOpt.isPresent() || nombreOpt.get().trim().isEmpty()) return;
        String nombre = nombreOpt.get().trim();

        Seccion seccion = inventario.encontrarSeccionDeLibro(nombre);
        if (seccion == null) { error("Libro no encontrado en ninguna sección."); return; }

        ObservableList<Libro> libros = seccion.encontrarLibrosPorTitulo(nombre);
        if (libros == null || libros.isEmpty()) { error("Libro no encontrado."); return; }

        if (libros.size() == 1) {
            seccion.venderLibro(nombre, libros.get(0).getIdInterno());
            info("Libro vendido con éxito!");
        } else {
            elegirEjemplar("Vender Ejemplar", libros, "Vender").ifPresent(l ->  {
                seccion.venderLibro(nombre, l.getIdInterno());
                info("Libro vendido con éxito!");
            });
        }
    }

    // ---------------------------------------------------------------
    // 5. Prestar Libro
    // ---------------------------------------------------------------
    private void prestarLibro() {
        Dialog<String[]> dlg = new Dialog<>();
        dlg.setTitle("Prestar Libro");
        dlg.setHeaderText("Ingrese los datos:");

        ButtonType okBtn = new ButtonType("Buscar", ButtonBar.ButtonData.OK_DONE);
        dlg.getDialogPane().getButtonTypes().addAll(okBtn, ButtonType.CANCEL);

        TextField fRut    = new TextField();
        TextField fNombre = new TextField();
        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(8);
        grid.setPadding(new Insets(15));
        grid.addRow(0, new Label("RUT del socio:"),    fRut);
        grid.addRow(1, new Label("Nombre del libro:"), fNombre);
        dlg.getDialogPane().setContent(grid);
        dlg.setResultConverter(bt -> bt == okBtn
                ? new String[]{fRut.getText().trim(), fNombre.getText().trim()} : null);

        Optional<String[]> res = dlg.showAndWait();
        if (!res.isPresent()) return;

        Socio socio = inventario.getSocio(res.get()[0]);
        if (socio == null) { error("Socio no encontrado."); return; }

        ObservableList<Libro> libros = inventario.encontrarLibro(res.get()[1]);
        if (libros == null || libros.isEmpty()) { error("Libro no encontrado."); return; }

        Libro libro;
        if (libros.size() == 1) {
            libro = libros.get(0);
        } else {
            Optional<Libro> libroOpt = elegirEjemplar("Seleccionar Ejemplar", libros, "Prestar");
            if (!libroOpt.isPresent()) return;
            libro = libroOpt.get();
        }

        boolean ok = inventario.prestarLibro(socio, libro);
        if (ok) info("Préstamo realizado con éxito!");
        else    error("El libro no es de tipo arrendable.");
    }

    // ---------------------------------------------------------------
    // 6. Buscar Libro
    // ---------------------------------------------------------------
    private void buscarLibro() {
        Optional<String> nombreOpt = pedirTexto("Buscar Libro", "Nombre del libro:");
        if (!nombreOpt.isPresent() || nombreOpt.get().trim().isEmpty()) return;

        ObservableList<Libro> libros = inventario.encontrarLibro(nombreOpt.get().trim());
        if (libros == null || libros.isEmpty()) { info("Libro no encontrado."); return; }

        StringBuilder sb = new StringBuilder();
        for (Libro l : libros) sb.append(libroToString(l)).append("\n---\n");
        mostrarTexto("Búsqueda", nombreOpt.get(), sb.toString());
    }

    // ---------------------------------------------------------------
    // 7. Guardar y Salir
    // ---------------------------------------------------------------
    private void guardarYSalir() {
        if (gestor == null) { stage.close(); return; }
        try {
            gestor.guardarTodo(inventario);
            info("Datos guardados con éxito!");
        } catch (Exception e) {
            error("Error al guardar: " + e.getMessage());
        }
        stage.close();
    }

    // ---------------------------------------------------------------
    // Utilidades de UI
    // ---------------------------------------------------------------

    private Button btn(String texto, javafx.event.EventHandler<javafx.event.ActionEvent> h) {
        Button b = new Button(texto);
        b.setOnAction(h);
        return b;
    }

    private void info(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.setHeaderText(null);
        a.showAndWait();
    }

    private void error(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        a.setHeaderText(null);
        a.showAndWait();
    }

    private Optional<String> pedirTexto(String titulo, String label) {
        TextInputDialog d = new TextInputDialog();
        d.setTitle(titulo);
        d.setHeaderText(null);
        d.setContentText(label);
        return d.showAndWait();
    }

    private void mostrarTexto(String titulo, String header, String contenido) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(titulo);
        a.setHeaderText(header);
        TextArea ta = new TextArea(contenido);
        ta.setEditable(false);
        ta.setPrefSize(420, 300);
        a.getDialogPane().setExpandableContent(ta);
        a.getDialogPane().setExpanded(true);
        a.showAndWait();
    }

    /** Muestra un ListView para elegir un ejemplar de una lista; devuelve el seleccionado. */
    private Optional<Libro> elegirEjemplar(String titulo, ObservableList<Libro> libros, String accion) {
        Dialog<Libro> dlg = new Dialog<>();
        dlg.setTitle(titulo);
        dlg.setHeaderText("Hay " + libros.size() + " ejemplares. Seleccione uno:");

        ListView<Libro> lv = new ListView<>(libros);
        lv.setCellFactory(l -> new ListCell<Libro>() {
            @Override protected void updateItem(Libro lb, boolean empty) {
                super.updateItem(lb, empty);
                setText(empty || lb == null ? ""
                        : "ID " + lb.getIdInterno() + " — " + lb.getTitulo());
            }
        });
        lv.getSelectionModel().selectFirst();
        lv.setPrefHeight(160);

        ButtonType okBtn = new ButtonType(accion, ButtonBar.ButtonData.OK_DONE);
        dlg.getDialogPane().getButtonTypes().addAll(okBtn, ButtonType.CANCEL);
        dlg.getDialogPane().setContent(lv);
        dlg.setResultConverter(bt -> bt == okBtn
                ? lv.getSelectionModel().getSelectedItem() : null);
        return dlg.showAndWait();
    }

    // ---------------------------------------------------------------
    // Serialización de libro a texto
    // ---------------------------------------------------------------
    private String libroToString(Libro l) {
        StringBuilder sb = new StringBuilder();
        sb.append("Título:   ").append(l.getTitulo()).append("\n");
        sb.append("Fecha:    ").append(l.getFechaDePublicacion()).append("\n");
        sb.append("Autores:  ").append(String.join(", ", l.getAutores())).append("\n");
        sb.append("Categoría:").append(l.getCategoria()).append("\n");
        sb.append("Edición:  ").append(l.getEdicion()).append("\n");
        sb.append("Páginas:  ").append(l.getPaginas()).append("\n");
        sb.append("Precio:   ").append(l.getPrecio()).append("\n");
        sb.append("ID:       ").append(l.getIdInterno()).append("\n");
        if (l instanceof LibroPrestable) {
            LibroPrestable lp = (LibroPrestable) l;
            sb.append("Disponible:      ").append(lp.getDisponibilidad()).append("\n");
            sb.append("Multa:           ").append(lp.getMulta()).append("\n");
            sb.append("Retraso:         ").append(lp.getRetraso()).append("\n");
            sb.append("Fecha préstamo:  ").append(lp.getFechaPrestamo()).append("\n");
            sb.append("Fecha devolución:").append(lp.getFechaDevolucion()).append("\n");
        } else if (l instanceof LibroDigital) {
            LibroDigital ld = (LibroDigital) l;
            sb.append("Formato:      ").append(ld.getFormato()).append("\n");
            sb.append("Memoria (MB): ").append(ld.getMemoria()).append("\n");
        }
        return sb.toString();
    }
}