Propertly
=========

Propertly is a java framework for data structures similar to java beans. As with java beans everything is defined solely with java objects.
The project is motivated by the many shortcomings java beans have.

Example of usage
----------------

A simple IPropertyPitProvider. Properties are first name, last name and age.
```java
// Generics describe parent, self and children
public class StudentPropertyPitProvider 
    extends AbstractPPP<IPropertyPitProvider, StudentPropertyPitProvider, Object>
{
  // IPropertyDescription gives static access to an IProperty's meta data like name and type.
  public static final IPropertyDescription<StudentPropertyPitProvider, String> FIRST_NAME =
      PD.create(StudentPropertyPitProvider.class);

  public static final IPropertyDescription<StudentPropertyPitProvider, String> LAST_NAME =
      PD.create(StudentPropertyPitProvider.class);

  public static final IPropertyDescription<StudentPropertyPitProvider, Integer> AGE =
      PD.create(StudentPropertyPitProvider.class);


  // Getters and setters can of course still be used for easier access. 
  public String getFirstName()
  {
    // getValue and setValue is available at AbstractPPP. That class is used for easier access. 
    // Propertly can be used without inheriting from that class, too.
    return getValue(FIRST_NAME);
  }

  public void setFirstName(String pFirstName)
  {
    setValue(FIRST_NAME, pFirstName);
  }

  public String getLastName()
  {
    return getValue(LAST_NAME);
  }

  public void setLastName(String pLastName)
  {
    setValue(LAST_NAME, pLastName);
  }

  public Integer getAge()
  {
    return getValue(AGE);
  }

  public void setAge(Integer pAge)
  {
    setValue(AGE, pAge);
  }

}
```

Using the defined provider:
```java
public class Sample
{

  public static void main(String[] args)
  {
    // Hierarchy is necessary to initialize the IPropertyPitProviders and for advanced features.
    Hierarchy<StudentPropertyPitProvider> hierarchy =
        new Hierarchy<>("student1", new StudentPropertyPitProvider());
    // The created student can be accessed from the hierarchy.
    StudentPropertyPitProvider student = hierarchy.getValue();
    // Listeners can be added.
    student.addPropertyEventListener(new PropertyPitEventAdapter()
    {
      @Override
      public void propertyChanged(IProperty pProperty, Object pOldValue, Object pNewValue)
      {
        System.out.println(pProperty.getName() + "=" + pNewValue);
      }
    });

    // The following calls will cause
    //  FIRST_NAME=Nils
    //  LAST_NAME=Holgersson
    //  AGE=32
    // to be printed on console through the listener.
    student.setFirstName("Nils");
    student.setLastName("Holgersson");
    student.setAge(32);
  }

}
```


Comparison to java beans:
--------------------------

Design decisions:

- No reflection. Interfaces are used to access models and fields. This way adaption is possible.
- Models and fields are defined using interfaces so that composition can be applied.
- Separation of store and access allows extensions.
- Listeners can be used at field, model and structure level.
- The tree can be navigated up and down.
- Dynamic models where fields can be added and removed at runtime are possible.


Additionally java beans power is preserved:

- Statically typed. Many errors can be noticed at compilation.
- Getter and setter can be provided and used.


The main draw backs compared to java beans are:

- Increased complexity.
- Generics are sometimes nasty.


Class overview:
---------------

The most important objects in Propertly are IPropertyDescription, IProperty, IPropertyPit, IPropertyPitProvider and IHierarchy:

- IPropertyDescription provides static meta data about an field.
- IProperty gives access to a fields data and it's meta data.
- IPropertyPit gives access to IProperty objects.
- IPropertyPitProvider describes an IPropertyPit and gives access to it.
- IHierarchy is the tree with IPropertyPitProvider objects as nodes.
