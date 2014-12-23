Propertly
=========

Propertly is a java framework for data structures similar to java beans. As with java beans everything is defined solely with java objects.
The project is motivated by the many shortcomings java beans have.


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


Overview:
---------

The most important objects in Propertly are, IPropertyDescription, IProperty, IPropertyPit, IPropertyPitProvider and IHierarchy:

- IPropertyDescription provides static meta data about an field.
- IProperty gives access to a fields data and it's meta data.
- IPropertyPit gives access to IProperty objects.
- IPropertyPitProvider describes an IPropertyPit and gives access to it.
- IHierarchy is the tree with IPropertyPitProvider objects as nodes.
