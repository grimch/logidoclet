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