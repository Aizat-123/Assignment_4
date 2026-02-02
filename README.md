A. SOLID Documentation:
SRP(Single Responsibility Principle)
Each class has one responsibility:
Controller: user interaction
Service: business logic
Repository: database access
Models: data and behavior
OCP (Open/Closed Principle)
New waste types (e.g. GlassWaste) can be added.
No need to modify existing service or controller code.
LSP (Liskov Substitution Principle)
PlasticWaste, PaperWaste, and ElectronicWaste can all be used as WasteItems.
Polymorphism works correctly.
ISP (Interface Segregation Principle)
Small and focused interfaces:
Chargeable, validatable, searchable. Classes implement only the interfaces they need.
DIP (Dependency Inversion Principle)
Controller depends on WasteItemsService interface.
Service depends on WasteRepository interface.

B. Advanced OOP Features
Generics used in the interface CrudRepository, the method sort in class SortingUtils.
Lambdas used in the when sorting by weight, filtering recyclable items, finding by criteria.
Reflection used here:ReflectionUtils.inspectClass(PlasticWaste.class);
It returns the fields, methods, parent class, implement interfaces, annotations of the class PlasticWaste.
Interface default Used here:
default boolean isValid() {
    try {
        validate();
        return true;
    } catch (Exception e) {
        return false;
    }
}
Static method used here:
static <T> boolean containsKeyword(T item, String keyword) {
    return item.toString().toLowerCase().contains(keyword.toLowerCase());

C. OOP Documentation
Abstract base class: BaseEntity.
Common fields: id, name.
Abstract methods: getType(), validate().
Concrete method: getInfo().
Abstract domain class: WasteItems extends BaseEntity and implements Chargeable, Validatable, Searchable.
Shared logic for all waste types
Polymorphic behavior
Subclasses, PlasticWaste, PaperWaste, ElectronicWaste.
Each subclass:
Overrides getWasteType().
Implements its own recycling cost logic.
Composition relationships:
RecyclingCenter has an Address
Demonstrated in both:
Java objects, Database tables with foreign keys

F. Execution Instructions
Database Setup Create database waste_management. Run the provided SQL schema file Driver Compile & Run. Click on run. 
H. Reflection
I gained practical experience working with:
abstract base classes and inheritance
interface-based polymorphis
lambdas, default and static methods in Interfaces
reflection to examine programm itself
Challenges Faced
One of the main challenges was maintaining a clean separation of concerns while using JDBC. It required careful design to ensure that database logic stayed inside the repository layer and did not leak into the service or controller layers.
SOLID attents you to bouild a flexible and adaptive program, that is easier to maintain and extend. Applying SOLID leads to better separation of concerns, making it simpler to add features or fix bugs without breaking existing functionality.
