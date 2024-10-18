package Controlador;

import Modelo.PersonaDAO;
import Modelo.Persona;
import Vista.Vista; 
import java.awt.event.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import java.util.List;
import javax.swing.JOptionPane;

public class Controlador implements ActionListener {

    PersonaDAO dao = new PersonaDAO();
    Persona p = new Persona();
    Vista V = new Vista();
    DefaultTableModel modelo = new DefaultTableModel();

    public Controlador(Vista V) {
        this.V = V;
        this.V.btnListar.addActionListener(this);
        this.V.btnGuardar.addActionListener(this);
        this.V.btnEditar.addActionListener(this); 
        this.V.btnEliminar.addActionListener(this);
        this.V.btnListo.addActionListener(this);
        this.V.btnFiltro.addActionListener(this); // Escuchar el botón de Filtro
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == V.btnListar) {
            Listar(V.Tabla);
        }
        if (e.getSource() == V.btnGuardar) {
            agregar();
        }
        if (e.getSource() == V.btnEditar) {
            editar();
        }
        if (e.getSource() == V.btnEliminar) {
            eliminar();
        }
        if (e.getSource() == V.btnListo) {
            cerrarVentana();
        }

        // Lógica para el botón Filtro
        if (e.getSource() == V.btnFiltro) {
            aplicarFiltro();
        }
    }

    public void Listar(JTable tabla) {
        modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0); // Limpiar la tabla antes de agregar nuevas filas
        List<Persona> lista = dao.listar(); 

        Object[] object = new Object[4];
        for (int i = 0; i < lista.size(); i++) {
            object[0] = lista.get(i).getId();
            object[1] = lista.get(i).getNombre();
            object[2] = lista.get(i).getCorreo();
            object[3] = lista.get(i).getTelefono();

            modelo.addRow(object);
        }
    }

    public void agregar() {
        String nom = V.tfNombre.getText();
        String corr = V.tfCorreo.getText();
        String tel = V.tfTelefono.getText();

        if (nom.isEmpty() || corr.isEmpty() || tel.isEmpty()) {
            JOptionPane.showMessageDialog(V, "Todos los campos deben ser completados");
            return; // Salir del método si hay campos vacíos
        }

        p.setNombre(nom);
        p.setCorreo(corr);
        p.setTelefono(tel);

        int respuesta = dao.Agregar(p);

        if (respuesta == 1) {
            JOptionPane.showMessageDialog(V, "Usuario Agregado");
            Listar(V.Tabla); // Listar después de agregar
        } else {
            JOptionPane.showMessageDialog(V, "Error al agregar el usuario");
        }
    }

    public void editar() {
        int fila = V.Tabla.getSelectedRow(); // Obtener fila seleccionada

        if (fila == -1) {
            JOptionPane.showMessageDialog(V, "Debe seleccionar una fila");
            return;
        }

        // Obtener el ID del empleado seleccionado
        int id = Integer.parseInt(V.Tabla.getValueAt(fila, 0).toString());
        String nom = V.tfNombre.getText();
        String corr = V.tfCorreo.getText();
        String tel = V.tfTelefono.getText();

        if (nom.isEmpty() || corr.isEmpty() || tel.isEmpty()) {
            JOptionPane.showMessageDialog(V, "Todos los campos deben ser completados");
            return;
        }

        // Configurar los nuevos valores
        p.setId(id);
        p.setNombre(nom);
        p.setCorreo(corr);
        p.setTelefono(tel);

        // Ejecutar la actualización
        int respuesta = dao.Editar(p);

        if (respuesta == 1) {
            JOptionPane.showMessageDialog(V, "Usuario Editado");
            Listar(V.Tabla); // Refrescar la tabla
        } else {
            JOptionPane.showMessageDialog(V, "Error al editar el usuario");
        }
    }

    public void eliminar() {
        int fila = V.Tabla.getSelectedRow(); // Obtener fila seleccionada

        if (fila == -1) {
            JOptionPane.showMessageDialog(V, "Debe seleccionar una fila");
            return;
        }

        // Obtener el ID del empleado seleccionado
        int id = Integer.parseInt(V.Tabla.getValueAt(fila, 0).toString());

        int respuesta = JOptionPane.showConfirmDialog(V, "¿Está seguro de que desea eliminar este empleado?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (respuesta == JOptionPane.YES_OPTION) {
            dao.Eliminar(id); // Eliminar el empleado
            JOptionPane.showMessageDialog(V, "Empleado Eliminado");
            Listar(V.Tabla); // Refrescar la tabla después de eliminar
        }
    }

    public void cerrarVentana() {
        V.dispose(); // Cerrar la ventana
    }

    // Método para aplicar el filtro en base a los campos de texto
    public void aplicarFiltro() {
        String nombre = V.tfNombre.getText();
        String correo = V.tfCorreo.getText();
        String telefono = V.tfTelefono.getText();

        if (!nombre.isEmpty()) {
            filtrarPorNombre(nombre);
        } else if (!correo.isEmpty()) {
            filtrarPorCorreo(correo);
        } else if (!telefono.isEmpty()) {
            filtrarPorTelefono(telefono);
        } else {
            JOptionPane.showMessageDialog(V, "Por favor, ingrese un nombre, correo o teléfono para filtrar.");
        }
    }

    // Método para filtrar por nombre
    public void filtrarPorNombre(String nombre) {
        modelo = (DefaultTableModel) V.Tabla.getModel();
        modelo.setRowCount(0); // Limpiar la tabla antes de mostrar los resultados
        List<Persona> lista = dao.filtrarPorNombre(nombre);

        Object[] object = new Object[4];
        for (int i = 0; i < lista.size(); i++) {
            object[0] = lista.get(i).getId();
            object[1] = lista.get(i).getNombre();
            object[2] = lista.get(i).getCorreo();
            object[3] = lista.get(i).getTelefono();
            modelo.addRow(object); // Añadir los resultados a la tabla
        }
    }

    // Método para filtrar por correo
    public void filtrarPorCorreo(String correo) {
        modelo = (DefaultTableModel) V.Tabla.getModel();
        modelo.setRowCount(0); // Limpiar la tabla antes de mostrar los resultados
        List<Persona> lista = dao.filtrarPorCorreo(correo);

        Object[] object = new Object[4];
        for (int i = 0; i < lista.size(); i++) {
            object[0] = lista.get(i).getId();
            object[1] = lista.get(i).getNombre();
            object[2] = lista.get(i).getCorreo();
            object[3] = lista.get(i).getTelefono();
            modelo.addRow(object); // Añadir los resultados a la tabla
        }
    }

    // Método para filtrar por teléfono
    public void filtrarPorTelefono(String telefono) {
        modelo = (DefaultTableModel) V.Tabla.getModel();
        modelo.setRowCount(0); // Limpiar la tabla antes de mostrar los resultados
        List<Persona> lista = dao.filtrarPorTelefono(telefono);

        Object[] object = new Object[4];
        for (int i = 0; i < lista.size(); i++) {
            object[0] = lista.get(i).getId();
            object[1] = lista.get(i).getNombre();
            object[2] = lista.get(i).getCorreo();
            object[3] = lista.get(i).getTelefono();
            modelo.addRow(object); // Añadir los resultados a la tabla
        }
    }
}
