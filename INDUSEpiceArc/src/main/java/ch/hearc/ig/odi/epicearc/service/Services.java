 package ch.hearc.ig.odi.epicearc.service;

import ch.hearc.ig.odi.epicearc.business.AbstractDate;
import ch.hearc.ig.odi.epicearc.business.ConiferSize;
import ch.hearc.ig.odi.epicearc.business.ConiferType;
import ch.hearc.ig.odi.epicearc.business.Customer;
import ch.hearc.ig.odi.epicearc.business.DeliveryDate;
import ch.hearc.ig.odi.epicearc.business.Order;
import ch.hearc.ig.odi.epicearc.business.PickupDate;
import ch.hearc.ig.odi.epicearc.business.Product;
import ch.hearc.ig.odi.epicearc.exception.IncorrectStateException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Singleton;

/**
 *
 * @author julien.plumez
 */
@Singleton
public class Services implements Serializable {

    /**
     * Contient l'état du stock de produits d'Epicé-Arc Le produit est utilisé
     * comme clé dans la map, et la quantité restante en stock est la valeur de
     * la map. A chaque nouvelle commande, la quantité est diminuée de 1,
     * jusqu'à 0.
     */
    private Map<Product, Integer> productsStock;

    /**
     * Contient la liste de toutes les commandes passées sur le site d'Epicé-Arc
     * L'ID de la commande est utilisé comme clé, et la commande comme valeur.
     * Cela permet de rechercher une commande de manière très rapide.
     */
    private Map<Long, Order> orders;

    /**
     * Contient la liste de toutes les dates de livraison et de retour
     * disponibles. L'ID de la date est utilisé comme clé, et la date comme
     * valeur. Cela permet de rechercher une date de manière très rapide.
     */
    private Map<Long, AbstractDate> dates;

    /**
     * Contient la liste de tous les clients L'ID du client est utilisé comme
     * clé, et le client comme valeur. Cela permet de rechercher un client de
     * manière très rapide.
     */
    private Map<Long, Customer> customers;

    /**
     * Conserve le dernier ID de Customer qui a été attribué, pour avoir des IDs
     * uniques.
     */
    private Long maxCustomerId = 1l;

    /**
     * Conserve le dernier ID de AbstractDate qui a été attribué, pour avoir des
     * IDs uniques.
     */
    private Long maxDateId = 1l;

    /**
     * Conserve le dernier ID de Order qui a été attribué, pour avoir des IDs
     * uniques.
     */
    private Long maxOrderId = 1l;

    /**
     * Conserve le dernier ID de Product qui a été attribué, pour avoir des IDs
     * uniques.
     */
    private Long maxProductId = 1l;

    public Services() {
        initData();
    }

    /**
     * Retourne la liste des différents types de conifères présents dans
     * l'application
     *
     * @return La liste des types de connifères
     */
    public List<ConiferType> getConiferTypes() {
        return Arrays.asList(ConiferType.values());
    }

    /**
     * Retourne la liste des produits encore disponibles pour un type de
     * conifère donné.
     *
     * @param coniferType Le type de conifère dont on souhaite connaître les
     * produits encore disponibles
     * @return La liste des produits disponibles pour le type de conifère
     */
    public List<Product> getProductsForConiferType(ConiferType coniferType) {
        List<Product> products = new ArrayList();

        for (Product currentProduct : productsStock.keySet()) {
            if (currentProduct.getConiferType() == coniferType && productsStock.get(currentProduct) > 0) {
                products.add(currentProduct);
            }
        }

        return products;
    }
    
    /**
     *
     * This method will look for the specific passed ID and return the corresponding product 
     * @param id of the target product
     * @return the target product 
     * @author Zakaria chentouf
     */
    public Product getProductsByID(Long id){
        Product p = null;
         for (Product currentProduct : productsStock.keySet()) {
            if (currentProduct.getId().equals(id)) {
                p = currentProduct;
                break;
            }
        }

        return p;
    }

    /**
     * Retourne la liste des dates de livraison encore disponibles pour un
     * produit donné. Si une date de livraison a déjà atteint sa pleine
     * capacité, elle ne sera pas retournée. Retourne une liste vide si aucune
     * date ne correspond.
     *
     * @param product Le produit à livrer
     * @return La liste des dates de livraison pour le produit
     */
    public List<DeliveryDate> getDeliveryDatesForProduct(Product product) {

        List<DeliveryDate> availableDates = new ArrayList();
        if (product != null) {

            for (AbstractDate currentDate : dates.values()) {
                if (currentDate instanceof DeliveryDate && ((DeliveryDate) currentDate).getConiferType() == product.getConiferType() && !isDeliveryDateFull(currentDate)) {
                    availableDates.add((DeliveryDate) currentDate);
                }
            }
        }

        return availableDates;
    }

    /**
     * Retourne la liste des dates de retour encore disponibles pour un produit
     * donné. Si une date de retour a déjà atteint sa pleine capacité, elle ne
     * sera pas retournée. Retourne une liste vide si aucune date ne correspond.
     *
     * @param product Le produit à retourner
     * @return La liste des dates de retour pour le produit
     */
    public List<PickupDate> getPickupDatesForProduct(Product product) {
        List<PickupDate> availableDates = new ArrayList();
        if (product != null) {
            for (AbstractDate currentDate : dates.values()) {
                if (currentDate instanceof PickupDate && ((PickupDate) currentDate).getConiferType() == product.getConiferType() && !isPickupDateFull(currentDate)) {
                    availableDates.add((PickupDate) currentDate);
                }
            }
        } 
        return availableDates;
    }

    /**
     * Sauvegarde une nouvelle commande dans le système Une validation de la
     * commande est effectuée, pour être sûr que tous les champs soient remplis
     *
     * @param order La commande à sauvegarder
     * @throws ch.hearc.ig.odi.epicearc.exception.IncorrectStateException Au cas
     * où la commande n'est pas complète et/ou contient des valeurs invalides
     */
    public synchronized void saveOrder(Order order) throws IncorrectStateException {
        if (order == null) {
            throw new NullPointerException("Can't validate a null Order");
        }

        if (!isProductInStock(order.getProduct())) {
            throw new IncorrectStateException("The product of this order is not in stock anymore.");
        }

        if (isDeliveryDateFull(order.getDeliveryDate())) {
            throw new IncorrectStateException("The delivery date of this order is already full");
        }

        if (isPickupDateFull(order.getPickupDate())) {
            throw new IncorrectStateException("The pickup date of this order is already full");
        }

        if (order.getId() != null) {
            throw new IncorrectStateException("This order already has an ID");
        }

        if (order.getCustomer() == null) {
            throw new IncorrectStateException("This order doesn't have a customer");
        } else if (!isCustomerValid(order.getCustomer())) {
            throw new IncorrectStateException("The customer of this order is incomplete.");
        }

        // OK, la commande est valide, on la sauvegarde
        // Si c'est un client qui n'est pas encore enregistré, on lui assigne un ID
        if (order.getCustomer().getId() == null) {
            order.getCustomer().setId(++maxCustomerId);
            Customer newCustomer = new Customer(order.getCustomer().getId(), order.getCustomer().getFirstName(), order.getCustomer().getLastName(), order.getCustomer().getAddress(), order.getCustomer().getZip(), order.getCustomer().getCity(), order.getCustomer().getPhone(), order.getCustomer().getEmail());

            customers.put(newCustomer.getId(), newCustomer);
        }

        // On fait une copie de l'objet Order, pour être sûr de ne pas avoir de problème de référence par la suite
        Order newOrder = new Order(maxOrderId++, new Date(), order.getAmount(), order.getDeliveryDate(), order.getPickupDate(), order.getProduct(), customers.get(order.getCustomer().getId()));

        orders.put(newOrder.getId(), newOrder);

        // On met à jour le stock puisque l'on vient de réaliser une vente.
        productsStock.put(newOrder.getProduct(), productsStock.get(newOrder.getProduct()) - 1);

    }
    private static final Logger LOG = Logger.getLogger(Services.class.getName());

    /**
     * Retourne les frais de transports à ajouter au montant de la facture Ce
     * sont des frais fixes de CHF 30.-
     *
     * @return Les frais de transport pour une commande
     */
    public Float getTransportCosts() {
        return 30.0f;
    }

    /**
     * Retourne la date correspondant à l'ID passé en paramètre, ou null si
     * aucune date ne correspond.
     *
     * @param id L'ID de l'AbstractDate
     * @return La date correspondant à l'ID, ou null
     */
    public AbstractDate getAbstractDateWithId(Long id) {
        return dates.get(id);
    }

    /**
     * Vérifie le nombre de commandes qui concernent un jour de livraison. Si le
     * nombre maximal de commandes est atteint, ce jour ne peut plus être
     * utilisé
     *
     * @param date La date à vérifier
     * @return true si le jour est déjà complet, false s'il reste de la place
     */
    private Boolean isDeliveryDateFull(AbstractDate date) {
        int nbOrders = 0;
        for (Order currentOrder : orders.values()) {
            if (currentOrder.getDeliveryDate() == date) {
                nbOrders++;
            }
        }

        return nbOrders >= 40;
    }

    /**
     * Vérifie le nombre de commandes qui concernent un jour de retour. Si le
     * nombre maximmal de commandes est atteint, ce jour ne peut plus être
     * utilisé
     *
     * @param date La date à vérifier
     * @return true si le jour est déjà complet, false s'il reste de la place
     */
    private Boolean isPickupDateFull(AbstractDate date) {
        int nbOrders = 0;
        for (Order currentOrder : orders.values()) {
            if (currentOrder.getPickupDate() == date) {
                nbOrders++;
            }
        }

        return nbOrders >= 40;
    }

    /**
     * Retourne true si la quantité restante pour ce produit est plus grande ou
     * égal à 1, ou false si ce produit n'est plus en stock
     *
     * @param product Le produit dont on souhaite connaître le stock
     * @return true si la quantité restante pour ce produit est plus grande ou
     * égal à 1, ou false si ce produit n'est plus en stock
     */
    private Boolean isProductInStock(Product product) {
        return productsStock.get(product) > 0;
    }

    /**
     * Vérifie que tous les champs de l'instance customer soient non-null et que
     * les chaînes de caractères ne soient pas vides
     *
     * @param customer Le client à vérifier
     * @return true si tous les champs sont valides, false s'il en manque un ou
     * plusieurs
     */
    public Boolean isCustomerValid(Customer customer) {
        if (customer == null) {
            return false;
        }

        return (!isNullOrEmpty(customer.getFirstName())
                && !isNullOrEmpty(customer.getLastName())
                && !isNullOrEmpty(customer.getAddress())
                && !isNullOrEmpty(customer.getZip())
                && !isNullOrEmpty(customer.getCity())
                && !isNullOrEmpty(customer.getPhone())
                && !isNullOrEmpty(customer.getEmail()));

    }

    private Boolean isNullOrEmpty(String string) {
        return string == null || string.trim().isEmpty();
    }

    /**
     * Initialise la liste des dates disponibles à la location et crée le stock
     * de produits.
     */
    private void initData() {
        createDates();
        createProductsStock();

        orders = new HashMap();
        customers = new HashMap();
    }

    /**
     * Crée des dates de livraison et de retour pour les différents types de
     * conifères
     */
    private void createDates() {
        try {
            dates = new HashMap();

            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

            dates.put(maxDateId, new DeliveryDate(maxDateId++, sdf.parse("16.12.2016"), ConiferType.SPRUCE));
            dates.put(maxDateId, new DeliveryDate(maxDateId++, sdf.parse("19.12.2016"), ConiferType.SPRUCE));
            dates.put(maxDateId, new DeliveryDate(maxDateId++, sdf.parse("20.12.2016"), ConiferType.SPRUCE));
            dates.put(maxDateId, new DeliveryDate(maxDateId++, sdf.parse("23.12.2016"), ConiferType.SPRUCE));

            dates.put(maxDateId, new DeliveryDate(maxDateId++, sdf.parse("16.12.2016"), ConiferType.NORDMANN));
            dates.put(maxDateId, new DeliveryDate(maxDateId++, sdf.parse("21.12.2016"), ConiferType.NORDMANN));

            dates.put(maxDateId, new DeliveryDate(maxDateId++, sdf.parse("16.12.2016"), ConiferType.NOBILIS));
            dates.put(maxDateId, new DeliveryDate(maxDateId++, sdf.parse("22.12.2016"), ConiferType.NOBILIS));

            dates.put(maxDateId, new PickupDate(maxDateId++, sdf.parse("03.01.2017"), ConiferType.SPRUCE));
            dates.put(maxDateId, new PickupDate(maxDateId++, sdf.parse("05.01.2017"), ConiferType.SPRUCE));
            dates.put(maxDateId, new PickupDate(maxDateId++, sdf.parse("06.01.2017"), ConiferType.SPRUCE));
            dates.put(maxDateId, new PickupDate(maxDateId++, sdf.parse("09.01.2017"), ConiferType.SPRUCE));

            dates.put(maxDateId, new PickupDate(maxDateId++, sdf.parse("03.01.2017"), ConiferType.NORDMANN));
            dates.put(maxDateId, new PickupDate(maxDateId++, sdf.parse("07.01.2017"), ConiferType.NORDMANN));

            dates.put(maxDateId, new PickupDate(maxDateId++, sdf.parse("03.01.2017"), ConiferType.NOBILIS));
            dates.put(maxDateId, new PickupDate(maxDateId++, sdf.parse("08.01.2017"), ConiferType.NOBILIS));
        } catch (ParseException ex) {
            Logger.getLogger(Services.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Initialise le catalogue de produits d'Epicé-Arc
     */
    private void createProductsStock() {
        productsStock = new HashMap();

        productsStock.put(new Product(maxProductId++, 90.0f, ConiferType.SPRUCE, ConiferSize.MINI), 20);
        productsStock.put(new Product(maxProductId++, 80.0f, ConiferType.SPRUCE, ConiferSize.SMALL), 10);
        productsStock.put(new Product(maxProductId++, 100.0f, ConiferType.SPRUCE, ConiferSize.MEDIUM), 50);
        productsStock.put(new Product(maxProductId++, 120.0f, ConiferType.SPRUCE, ConiferSize.BIG), 20);

        productsStock.put(new Product(maxProductId++, 120.0f, ConiferType.NORDMANN, ConiferSize.MINI), 0);
        productsStock.put(new Product(maxProductId++, 95.0f, ConiferType.NORDMANN, ConiferSize.SMALL), 50);
        productsStock.put(new Product(maxProductId++, 110.0f, ConiferType.NORDMANN, ConiferSize.MEDIUM), 30);
        productsStock.put(new Product(maxProductId++, 150.0f, ConiferType.NORDMANN, ConiferSize.BIG), 20);

        productsStock.put(new Product(maxProductId++, 150.0f, ConiferType.NOBILIS, ConiferSize.MINI), 0);
        productsStock.put(new Product(maxProductId++, 110.0f, ConiferType.NOBILIS, ConiferSize.SMALL), 0);
        productsStock.put(new Product(maxProductId++, 150.0f, ConiferType.NOBILIS, ConiferSize.MEDIUM), 50);
        productsStock.put(new Product(maxProductId++, 200.0f, ConiferType.NOBILIS, ConiferSize.BIG), 50);

    }

}
