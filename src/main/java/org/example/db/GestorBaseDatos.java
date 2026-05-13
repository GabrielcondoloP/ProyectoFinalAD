package org.example.db;

import org.example.model.Producto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;


public class GestorBaseDatos {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoFinalPU");

    // 1. ALTA (Crear)
    public void guardar(Producto p) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(p);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // 2. CONSULTA GENERAL (Listado para la tabla gráfica)
    public List<Producto> obtenerTodos() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT p FROM Producto p", Producto.class).getResultList();
        } finally {
            em.close(); // Siempre cerramos el gestor para liberar recursos
        }
    }

    // 3. BÚSQUEDA POR NOMBRE (Requisito obligatorio)
    public List<Producto> buscarPorNombre(String texto) {
        EntityManager em = emf.createEntityManager();
        try {
            // Usamos JPQL para buscar por texto parcial usando LIKE
            return em.createQuery("SELECT p FROM Producto p WHERE p.nombre LIKE :nombre", Producto.class)
                    .setParameter("nombre", "%" + texto + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // 4. MODIFICACIÓN
    public void actualizar(Long id, String nuevoNombre, String nuevaDesc, double nuevoPrecio, int nuevoStock) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            // 1. Leer la entidad
            Producto p = em.find(Producto.class, id);

            if (p != null) {
                // 2. Modificar atributos
                p.setNombre(nuevoNombre);
                p.setDescripcion(nuevaDesc);
                p.setPrecio(nuevoPrecio);
                p.setStock(nuevoStock);
            }

            // 3. Commit de la transacción (sincroniza los cambios)
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // 5. BORRADO
    public void eliminar(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Producto p = em.find(Producto.class, id);
            if (p != null) {
                em.remove(p);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // Metodo para apagar la conexión al cerrar el programa
    public void cerrar() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}