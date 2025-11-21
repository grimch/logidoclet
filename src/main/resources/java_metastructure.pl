% META-DOCUMENTATION
%
% This file defines the schema for all other Prolog facts in this documentation set.
% It serves as the single source of truth for understanding the structure of the data.

% HOW TO BOOTSTRAP:
%
% To begin parsing the project structure, check for the existence of one of the
% following entry-point predicates in the 'minimal/' directory:
%
%   1. module_index/1:  If present, the project is modular. Start here.
%   2. package_index/1: If present, the project is non-modular. Start here.
%
% From the package names found, navigate to the corresponding '[package_path]/package.pl'
% file, which contains type_declaration/2 facts for all types in that package.

% SCHEMA PREDICATES:
%
% The schema is defined by two main predicates:
%
%   - predicate_info(PredicateName, arity(Arity)):
%     Describes a predicate, its name, and its arity (number of arguments).
%     Example: predicate_info(class, arity(10)).
%
%   - argument_info(PredicateName, ArgIndex, ArgName, type(TypeSpecifier)):
%     Describes a specific argument of a predicate.
%     - ArgIndex: The 1-based position of the argument.
%     - ArgName: A human-readable name for the argument.
%     - TypeSpecifier: A wrapper defining the data type of the argument.
%
%     Example: argument_info(class, 1, name, type(simple_name)).
%     This means: "The first argument of the class/10 predicate is named 'name'
%                  and its type is 'simple_name'."

% TYPE REPRESENTATION:
%
% Types are represented using a structured hierarchy.
%
%   - A simple named type is a wrapper around an atom, e.g., simple_name('MyClass').
%     The possible wrappers (simple_name, qualified_name, etc.) are defined throughout this file.
%
%   - A declared type is a fully qualified type, potentially with generics:
%     declared_type(QualifiedName, TypeArguments).
%     Example: declared_type('java.util.List', [declared_type('java.lang.String', [])]).
%
%   - A generic or primitive type representation uses the type/2 predicate:
%     type(Kind, Detail).
%     - 'Kind': The category of type, defined by type_kind_atom/1 (e.g., primitive, wildcard_extends).
%     - 'Detail': The specific type name (for primitives) or its bound.
%     Example (primitive): type(primitive, int).
%     Example (generic):   type(wildcard_extends, declared_type('java.lang.Number', [])).

% Any type not explicitly defined by a 'predicate_info' fact in this document is to be considered an atomic type.

predicate_info(package_index, arity(1)).
argument_info(package_index, 1, modules, type(list(package_name))).

predicate_info(module_index, arity(1)).
argument_info(module_index, 1, modules, type(list(module_name))).

predicate_info(module, arity(7)).
argument_info(module, 1, name, type(module_name)).
argument_info(module, 2, modifiers, type(list(modifier))). % e.g., 'open'
argument_info(module, 3, requires, type(list(requires))).
argument_info(module, 4, exports, type(list(exports))).
argument_info(module, 5, uses, type(list(type))). % Services consumed
argument_info(module, 6, provides, type(list(provides))). % Services offered
argument_info(module, 7, all_packages, type(list(package_name))).

predicate_info(requires, arity(3)).
argument_info(requires, 1, modifiers, type(list(modifier))). % e.g., 'transitive', 'static'
argument_info(requires, 2, module_name, type(module_name)).
argument_info(requires, 3, annotations, type(list(annotation))).

predicate_info(exports, arity(3)).
argument_info(exports, 1, package_name, type(package_name)).
argument_info(exports, 2, to_modules, type(list(module_name))). % Target modules, or '_' if public
argument_info(exports, 3, annotations, type(list(annotation))).

predicate_info(provides, arity(3)).
argument_info(provides, 1, service_type, type(type)).
argument_info(provides, 2, with_implementations, type(list(type))).
argument_info(provides, 3, annotations, type(list(annotation))).

predicate_info(package_declaration, arity(2)).
argument_info(package_declaration, 1, package_name, type(package_name)).
argument_info(package_declaration, 2, declared_types, type(list(type_declaration))).

predicate_info(type_declaration, arity(2)).
argument_info(type_declaration, 1, name, type(simple_name)).
argument_info(type_declaration, 2, category, type(type_category)).

predicate_info(class, arity(10)).
argument_info(class, 1, name, type(simple_name)).
argument_info(class, 2, package_name, type(package_name)).
argument_info(class, 3, modifiers, type(list(modifier))).
argument_info(class, 4, type_parameters, type(list(type_parameter))).
argument_info(class, 5, extends, type(extends)).
argument_info(class, 6, implements, type(list(implements))).
argument_info(class, 7, permitted_subclasses, type(list(qualified_name))).
argument_info(class, 8, members, type(list(member))).
argument_info(class, 9, annotations, type(list(annotation))).
argument_info(class, 10, doc_comment, type(string)).

predicate_info(interface, arity(9)).
argument_info(interface, 1, name, type(simple_name)).
argument_info(interface, 2, package_name, type(package_name)).
argument_info(interface, 3, modifiers, type(list(modifier))).
argument_info(interface, 4, type_parameters, type(list(type_parameter))).
argument_info(interface, 5, extends, type(list(extends))).
argument_info(interface, 6, members, type(list(member))).
argument_info(interface, 7, annotations, type(list(annotation))).
argument_info(interface, 8, permitted_implementations, type(list(declared_type))).
argument_info(interface, 9, doc_comment, type(string)).

predicate_info(record, arity(9)).
argument_info(record, 1, name, type(simple_name)).
argument_info(record, 2, package_name, type(package_name)).
argument_info(record, 3, modifiers, type(list(modifier))).
argument_info(record, 4, type_parameters, type(list(type_parameter))).
argument_info(record, 5, implements, type(list(implements))).
argument_info(record, 6, components, type(list(record_component))).
argument_info(record, 7, members, type(list(member))).
argument_info(record, 8, annotations, type(list(annotation))).
argument_info(record, 9, doc_comment, type(string)).

predicate_info(enum, arity(8)).
argument_info(enum, 1, name, type(simple_name)).
argument_info(enum, 2, package_name, type(package_name)).
argument_info(enum, 3, modifiers, type(list(modifier))).
argument_info(enum, 4, implements, type(list(implements))).
argument_info(enum, 5, constants, type(list(enum_constant))).
argument_info(enum, 6, members, type(list(member))).
argument_info(enum, 7, annotations, type(list(annotation))).
argument_info(enum, 8, doc_comment, type(string)).

predicate_info(enum_constant, arity(3)).
argument_info(enum_constant, 1, name, type(simple_name)).
argument_info(enum_constant, 2, annotations, type(list(annotation))).
argument_info(enum_constant, 3, constructor_arguments, type(list(term))).

predicate_info(annotation_type, arity(6)).
argument_info(annotation_type, 1, name, type(simple_name)).
argument_info(annotation_type, 2, package_name, type(package_name)).
argument_info(annotation_type, 3, modifiers, type(list(modifier))).
argument_info(annotation_type, 4, members, type(list(annotation_member))).
argument_info(annotation_type, 5, annotations, type(list(annotation))).
argument_info(annotation_type, 6, doc_comment, type(string)).

predicate_info(member, arity(1)).
argument_info(member, 1, member_fact, type(term)).

predicate_info(method, arity(8)).
argument_info(method, 1, name, type(simple_name)).
argument_info(method, 2, modifiers, type(list(modifier))).
argument_info(method, 3, type_parameters, type(list(type_parameter))).
argument_info(method, 4, return_type, type(type)).
argument_info(method, 5, parameters, type(list(parameter))).
argument_info(method, 6, throws, type(list(throws))).
argument_info(method, 7, annotations, type(list(annotation))).
argument_info(method, 8, doc_comment, type(string)).

predicate_info(constructor, arity(7)).
argument_info(constructor, 1, name, type(simple_name)).
argument_info(constructor, 2, modifiers, type(list(modifier))).
argument_info(constructor, 3, type_parameters, type(list(type_parameter))).
argument_info(constructor, 4, parameters, type(list(parameter))).
argument_info(constructor, 5, throws, type(list(throws))).
argument_info(constructor, 6, annotations, type(list(annotation))).
argument_info(constructor, 7, doc_comment, type(string)).

predicate_info(field, arity(5)).
argument_info(field, 1, name, type(simple_name)).
argument_info(field, 2, modifiers, type(list(modifier))).
argument_info(field, 3, type, type(type)).
argument_info(field, 4, annotations, type(list(annotation))).
argument_info(field, 5, doc_comment, type(string)).

predicate_info(annotation_member, arity(5)).
argument_info(annotation_member, 1, name, type(simple_name)).
argument_info(annotation_member, 2, return_type, type(type)).
argument_info(annotation_member, 3, default_value, type(term)).
argument_info(annotation_member, 4, annotations, type(list(annotation))).
argument_info(annotation_member, 5, text_block, type(string)).

predicate_info(parameter, arity(4)).
argument_info(parameter, 1, name, type(simple_name)).
argument_info(parameter, 2, type, type(type)).
argument_info(parameter, 3, modifiers, type(list(modifier))).
argument_info(parameter, 4, annotations, type(list(annotation))).

predicate_info(record_component, arity(3)).
argument_info(record_component, 1, name, type(simple_name)).
argument_info(record_component, 2, type, type(type)).
argument_info(record_component, 3, annotations, type(list(annotation))).

predicate_info(declared_type, arity(2)).
argument_info(declared_type, 1, qualified_name, type(qualified_name)).
argument_info(declared_type, 2, type_arguments, type(list(type))).

predicate_info(type, arity(2)).
argument_info(type, 1, type_kind, type(type_kind_atom)).
argument_info(type, 2, type_name_or_detail, type(term)).

predicate_info(type_parameter, arity(3)).
argument_info(type_parameter, 1, name, type(simple_name)).
argument_info(type_parameter, 2, bounds, type(list(type))).
argument_info(type_parameter, 3, annotations, type(list(annotation))).

predicate_info(annotation, arity(2)).
argument_info(annotation, 1, name, type(qualified_name)).
argument_info(annotation, 2, arguments, type(list(annotation_argument))).

predicate_info(annotation_argument, arity(2)).
argument_info(annotation_argument, 1, name, type(simple_name)).
argument_info(annotation_argument, 2, value, type(term)).

predicate_info(modifier, arity(1)).
argument_info(modifier, 1, keyword, type(modifier_keyword)).

predicate_info(extends, arity(2)).
argument_info(extends, 1, type_kind, type(type_kind_atom)).
argument_info(extends, 2, target_type, type(type)).

predicate_info(implements, arity(2)).
argument_info(implements, 1, type_kind, type(type_kind_atom)).
argument_info(implements, 2, target_type, type(type)).

predicate_info(throws, arity(1)).
argument_info(throws, 1, exception_type, type(type)).

predicate_info(module_name, arity(1)).
argument_info(module_name, 1, name, type(atom)).

predicate_info(package_name, arity(1)).
argument_info(package_name, 1, name, type(atom)).

predicate_info(simple_name, arity(1)).
argument_info(simple_name, 1, name, type(atom)).

predicate_info(qualified_name, arity(1)).
argument_info(qualified_name, 1, name, type(atom)).

predicate_info(type_category, arity(1)).
argument_info(type_category, 1, name, type(atom)).

predicate_info(type_kind_atom, arity(1)).
argument_info(type_kind_atom, 1, name, type(atom)).

predicate_info(modifier_keyword, arity(1)).
argument_info(modifier_keyword, 1, name, type(atom)).

% Defines the possible categories for a type declaration.
type_category('CLASS').
type_category('INTERFACE').
type_category('ENUM').
type_category('RECORD').
type_category('ANNOTATION').

% Defines the possible keywords for a modifier.
modifier_keyword(public).
modifier_keyword(protected).
modifier_keyword(private).
modifier_keyword(abstract).
modifier_keyword(static).
modifier_keyword(final).
modifier_keyword(synchronized).
modifier_keyword(volatile).
modifier_keyword(transient).
modifier_keyword(native).
modifier_keyword(strictfp).
modifier_keyword(sealed).
modifier_keyword('non-sealed').

% Defines the kinds of types, used in type/2, extends/2, etc.
type_kind_atom(primitive).
type_kind_atom(wildcard_extends).
type_kind_atom(wildcard_super).
type_kind_atom(wildcard).
type_kind_atom(declared).

% Defines the set of Java primitive types.
predicate_info(primitive_type, arity(1)).
argument_info(primitive_type, 1, name, type(atom)).

primitive_type(boolean).
primitive_type(byte).
primitive_type(char).
primitive_type(short).
primitive_type(int).
primitive_type(long).
primitive_type(float).
primitive_type(double).
primitive_type(void).

% --- EXECUTABLE SCHEMA RULES ---

% Defines the valid entry-point predicates for project discovery.
is_entry_point(module_index).
is_entry_point(package_index).

% Retrieves the type specifier for a given argument of a predicate.
% Example: argument_type(class, 1, Type). -> Type = simple_name.
argument_type(PredicateName, ArgIndex, TypeSpecifier) :-
    argument_info(PredicateName, ArgIndex, _, type(TypeSpecifier)).

% Succeeds if a given TypeName and Category form a valid type declaration
% according to the master schema. It checks if the Category is a known one.
is_valid_type_declaration(_TypeName, Category) :-
    type_category(Category),
    true.

% Succeeds if a given ModifierKeyword is a valid modifier
% according to the master schema.
is_valid_modifier(ModifierKeyword) :-
    modifier_keyword(ModifierKeyword).