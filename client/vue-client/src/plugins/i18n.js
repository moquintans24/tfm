import Vue from "vue";
import VueI18n from "vue-i18n";
import store from "../common/store";

Vue.use(VueI18n);

const messages = {
    "es-ES": {
        welcomeMsg: 'Bienvenido a tu aplicación Vue.js',
        digital_content:'Contenido digital',
        logged_as:'Registrado/a como',
        home:'Inicio',
        books:'Libros',
        statistics:'Estadísticas',
        classes:'Clases',
        class_groups:'Grupos de clase',
        login:'Iniciar sesión',
        logout:'Cerrar sesión',
        create:'Crear',
        passwd: 'Contraseña',
        code:'Código',
        center:'Centro',
        language:'Idioma',
        teacher:'Profesor/a',
        teachers:'Profesorado',
        student:'Alumno/a',
        students:'Alumnado',
        admin:'Administrador/a',
        editor:'Editor/a',
        editors:'Editores/as',
        role:'Rol',
        book:'Libro',
        title:'Título',
        course:'Curso',
        level:'Nivel',
        see_book:'Ver libro',
        codes:'Códigos',
        search:'Buscar',
        create_class_group:'Crear grupo de clase',
        create_account: 'Crear cuenta',
        forgot_passwd: '¿Has olvidado tu contraseña?',
        required_passwd: 'La contraseña es obligatoria',
        required_email: 'El email es obrigatorio',
        required_login: 'El login es obligatorio',
        register: 'Registrarse',
        repeat_passwd: 'Repetir contraseña',
        required_code: 'El código es obligatorio',
        required_name: 'El nombre es obligatorio',
        required_last_name: 'El apellido es obligatorio',
        name: 'Nombre',
        last_name: 'Apellidos',
        birthdate: 'Fecha de nacimiento',
        required_birthdate: 'La fecha de nacimiento es obligatoria',
        required_language: 'El idioma es obligatorio',
        noBooks:'No hay ningún libro',
        add: 'Añadir',
        description: 'Descripción',
        required_title: 'El título es obligatorio',
        required_level: 'El nivel es obligatorio',
        required_course: 'El curso es obligatorio',
        required_description: 'La descripción es obligatoria',
        tooltip_to_book_code_teacher: 'El alumnado solo podrá ver el contenido habilitado por el profesor/a',
        tooltip_to_book_code_student: 'El libro no tendrá profesor/a asociado. El alumnado podrá ver todo el contenido del libro',
        insert: 'Introduzca',
        the: 'el',
        preposition:'de la',
        text_new_class: 'Introduzca el nombre de la clase vinculada al libro',
        required_class_name: 'El nombre de la clase es obligatorio',
        create_new_class:'Crear nueva clase',
        required_select_book: 'El libro es obligatorio',
        text_new_class_from_classes:'Intruduzca el libro y el nombre de la clase',
        user_management: 'Gestión de usuarios',
        content: 'Contenido',
        content_editor:'editor de contenido',
        content_editors:'Editores de contenido',
        rows_per_page:'Filas por página',
        register_date:'Fecha de registro',
        actions: 'Acciones',
        action:'Acción',
        wish: 'desea',
        question:'¿',
        create_date: 'Fecha de creación',
        create_date_start: 'Fecha de creación inicio',
        create_date_end: 'Fecha de creación fin',
        expiration_date: 'Fecha de caducidad',
        generated_by: 'Generado por',
        create_codes: 'Generar códigos',
        news: 'Nuevos',
        create_new_codes: 'Indique el número de códigos a generar y la fecha de caducidad',
        required_file_name:'El nombre del fichero es obligatorio',
        required_expired_date: 'La fecha de caducidad es obligatoria',
        required_number_codes: 'El número de códigos es obligatorio',
        format_file:'Formato del fichero',
        export_codes: 'Indique las fechas de creación entre las que están comprendidas los códigos a exportar',
        export_creation_date:'La fecha de creación es obligatoria',
        date_between:'Fechas comprendidas entre',
        file_name:'Nombre del fichero',
        no_codes_in_dates: 'No hay códigos creados entre las fechas indicadas',
        rules_creation_date_start: 'La fecha de creación inicio debe ser menor que la fecha de creación fin',
        rules_creation_date_end: 'La fecha de creación fin debe ser mayor que la fecha de creación inicio',
        unsubscribe: 'Dar de baja',
        unsubscribe_user: 'Dar de baja usuario',
        unsubscribe_user_question:'¿Está seguro que desea dar de baja al usuario ',
        unit:'Unidad',
        lesson:'Lección',
        class_group_name: 'Nombre del grupo',
        book_state_ready: 'Listo',
        book_state_pending: 'Pendiente',
        book_state_completed:'Contenido terminado',
        book_state_archived:'Archivado',
        total_subscriptions:'Total suscripciones',
        total_class_groups:'Total grupos de clase',
        total_centers:'Total centros',
        delete_book:'Eliminar libro',
        cancel:'Cancelar',
        ok:'Aceptar',
        export:'Exportar',
        state:'Estado',
        permissions:'Permisos',
        users:'Usuarios',
        profile:'Perfil',
        question_delete_book:'¿Está seguro que desea eliminar el libro?',
        new_codes:'Nuevos códigos',
        title_export_codes:'Exportar códigos',
        class_group_code:'Código del grupo de clase',
        select_book:'Seleccione un libro',
        no_classes_created: 'No tiene clases creadas',
        no_classes_assigned: 'No tiene clases asignadas',
        delete_class:'Eliminar clase',
        delete_class_question: '¿Seguro que desea eliminar la clase ',
        edit_class:'Editar clase',
        edit:'Editar',
        class_code_copied:'Código de clase copiado',
        class_content:'Contenido',
        class_activities:'Actividades',
        class_users:'Alumnado',
        show:'Mostrar',
        hide:'Oculta',
        end_date:'Fecha fin',
        students:'Alumnado',
        enable_activity:'Habilitar actividad',
        homework_activity:'Deberes',
        see_activity_students:'Ver alumnos',
        duration:'Duración',
        score:'Puntuación',
        activity_user_list_table_title:'Alumnado que realizó la actividad ',
        required_login: 'El login es obligatorio',
        valid_email:'El email debe ser válido',
        password_match:'Las contraseñas deben coincidir',
        add_book: 'Añadir libro',
        add_code_book:'Introduzca el código del libro',
        subscription_completed_state:'Solicitud aceptada',
        subscription_cancelled_state:'Solicitud anulada',
        subscription_pending_state:'Solicitud pendiente',
        no_students_realized_activity:'Ningún alumno ha realizo la actividad',
        user_deleted_class_group: 'Usuario eliminado del grupo de clase',
        delete:'Eliminar',
        assign_activity:'Asignar actividad',
        hour:'Hora',
        required_hour:'La hora es obligatoria',
        required_date:'La fecha es obligatoria',
        pending_activities:'Actividades pendientes',
        explore_activities:'Explorar actividades',
        activity:'Actividad',
        no_pending_activities:'No tiene actividades pendientes',
        no_pending_books: 'No hay libros pendientes',
        drop_file_click_select: 'Suelte aquí el archivo o haga click para seleccionarlo.',
        book_with_code:'Libro con código de ',
        extension_support: 'Extensión soportada',
        please_select_image_file:'Por favor, seleccione una image',
        only_one_file:'Solo se puede subir un archivo',
        duplicate:'Duplicar',
        duplicate_book:'Duplicar libro',
        question_duplicate_book: '¿Está seguro que desea duplicar el libro ',
        required_unit_number: 'El número de unidad es obligatorio',
        number:'Número',
        delete_unit:'Eliminar unidad',
        question_delete_unit:'¿Está seguro que desea eliminar la unidad ',
        required_lesson_number:'El número de lección es obligatorio',
        delete_lesson:'Eliminar lección',
        question_delete_lesson:'¿Está seguro que desea eliminar la lección ',
        add_activity:'Añadir actividad',
        please_select_pdf_h5p_file:'Por favor, seleccione un fichero pdf o h5p',
        activity_file:'Fichero de la actividad',
        activity_image:'Imagen de la actividad',
        required_number:'El número es obligatorio',
        edit_activity:'Editar actividad',
        delete_activity:'Eliminar actividad',
        question_delete_activity:'¿Seguro que desea eliminar la actividad ',
        see_presentation_book:'Ver modo presentación',
        see_edition_book:'Ver modo edición',
        edit_book:'Editar libro',
        forgotten_password:'¿Has olvidado tu contraseña?',
        reset_password:'Introduce tu dirección de correo o login para recuperar tu contraseña',
        send:'Enviar',
        required_email_login:'El email o login es obligatorio',
        email_sent:'Email enviado',
        text_check_email:'El email ha sido enviado, por favor, revisa tu correo para cambiar la contraseña',
        new_password:'Introduzca la nueva contraseña',
        reset_password:'Restablecer contraseña',
        password_changed:'Contraseña cambiada',
        password_established:'Se ha establecido la nueva contraseña',
        establish_login_password:'Establezca el login y contraseña',
        text_establish_login_password:'Por favor, introduzca su login y contraseña para poder iniciar sesión en la aplicación de contenido digital',
        login_password_established:'Login y contraseña establecidos',
        all:'Todo',
        no_content_visible:'Todavía no tiene contenido visible',
        enabled:'Habilitada',
        disabled:'Deshabilitado',
        edit_end_date:'Editar la fecha de fin',
        book_content:'Contenido del libro',
        search_by_activity_description:'Buscar por descripción de actividad',
        search_by_lesson:'Buscar por lección',
        search_by_unit:'Buscar por unidad',
        search_by_user_fullname:'Buscar por nombre completo de usuario',
        search_by_book_title:'Buscar por título de libro',
        search_by_course:'Buscar por curso',
        search_by_level:'Buscar por nivel',
        activity_not_found:'No se encontró la actividad',
        save:'Guardar',
        no_books_subscriptions:'No tiene ninguna suscripción',
        finish:'Terminar',
        discard:'Descartar',
        new:'Nuevo',
        delete:'Eliminar',
        download:'Descargar',
        no_pending_books:'No hay libros pendientes',
        image:'Imagen',
        new_class_subscription:'Suscribirse grupo de clase',
        add_class_code:'Introduce el código del grupo de clase',
        pending_subscription:'La solicitud para suscribirse al grupo de clase aún no ha sido aceptada',
        cancelled_subscription:'La solicitud para subscribirse al grupo de clase ha sido cancelada',
        no_activities_assigned:'No tiene actividades asignadas',
        loading_activities:'Cargando actividades...',
        loading_activity:'Cargando actividad...',
        no_class_groups_created:'No tiene grupos de clase creados',
        score_out_of:'Puntuación sobre ',
        users_grades:'Calificaciones',
        avg_score:'Puntuación media',
        avg_homework_score:'Puntuación media de deberes',
        avg_not_assigned_act_score:'Puntuación media de actividades non asignadas',
        avg_duration:'Duracion media',
        activities_done: 'Actividades realizadas',
        activities_undone:'Actividades no realizadas',
        avg_class_group_score:'Puntuación media del grupo de clase ',
        avg_class_group_score_by_unit:'Puntuación media del grupo de clase en la unidad',
        global_:'Global',
        no_activities_to_do:'El alumnado no tiene actividades para realizar',
        avg_duration_in_min: 'Duración media (minutos)',
        all_activities:'Todas las actividades',
        activities_done:'Actividades realizadas',
        activities_undone:'Actividades sin realizar',
        without_end_date:'Sin fecha de entrega',
        authority:'Permisos',
        book_code:'Código de libro',
        required_class_group_code:'El código del grupo de clase es obligatorio',
        required_book_code:'El código del libro es obligatorio',
        add_class_group_code_and_book_code:'Introduzca el código del grupo de clase y el código del libro digital',
        subscription_class_group:'Suscribirse a un grupo de clase',
        activities_with_same_h5p_content:'No se puede eliminar el contenido h5p, tiene más actividades asociadas',
        book_in_use:'En uso',
        search_by_book_state:'Buscar por estado',
        code_state_used:'Usado',
        code_state_new:'Nuevo'
    },
    "gl-ES": {
        welcomeMsg: "Benvido a tua aplicación Vue.js",
        digital_content:'Contido dixital',
        logged_as:'Rexistrado/a como',
        home:'Inicio',
        books:'Libros',
        statistics:'Estadísticas',
        classes:'Clases',
        class_groups:'Grupos de clase',
        login:'Iniciar sesión',
        logout:'Cerrar sesión',
        create:'Crear',
        passwd: 'Contrasinal',
        code:'Código',
        center:'Centro',
        language:'Idioma',
        teacher:'Profesor/a',
        teachers:'Profesorado',
        student:'Alumno/a',
        students:'Alumnado',
        admin:'Administrador/a',
        editor:'Editor/a',
        editors:'Editores/as',
        role:'Rol',
        book:'Libro',
        title:'Título',
        course:'Curso',
        level:'Nivel',
        see_book:'Ver libro',
        codes:'Códigos',
        search:'Buscar',
        create_account:'Crear conta',
        create_class_group:'Crear grupo de clase',
        forgot_passwd: 'Esqueceu o seu contrasinal?',
        required_passwd: 'O contrasinal é obrigatorio',
        required_email: 'O email é obrigatorio',
        required_login: 'O login é obrigatorio',
        register: 'Rexistrarse',
        repeat_passwd: 'Repetir contrasinal',
        required_code: 'O código é obrigatorio',
        required_name: 'O nome é obligatorio',
        required_last_name: 'O apelido é obrigatorio',
        name: 'Nome',
        last_name: 'Apelidos',
        birthdate: 'Data de nacemento',
        required_birthdate: 'A data de nacemento é obrigatoria',
        required_language: 'O idioma é obrigatorio',
        noBooks: 'Non hay ningún libro',
        add:'Engadir',
        description: 'Descrición',
        required_title: 'O título é obrigatorio',
        required_level: 'O nivel é obrigatorio',
        required_course: 'O curso é obrigatorio',
        required_description: 'A descrición é obrigatorio',
        tooltip_to_book_code_teacher: 'O alumnado so poderá ver o contido habilitado polo profesor/a',
        tooltip_to_book_code_student: 'O libro non terá profesor/a asociado. O alumnado poderá ver todo o contido do libro',
        insert: 'Introduza',
        the: 'o',
        preposition: 'da',
        text_new_class: 'Introduza o nome da clase vinculada ó libro',
        required_class_name: 'O nome da clase é obrigatorio',
        create_new_class: 'Crear nova clase',
        required_select_book: 'O libro é obrigatorio',
        text_new_class_from_classes: 'Introduza o libro e o nome da clase',
        user_management: 'Xestión de usuarios',
        content: 'Contido',
        content_editor:'editor de contido',
        content_editors:'Editores de contido',
        rows_per_page:'Filas por páxina',
        register_date:'Data de rexistro',
        actions:'Accións',
        action:'Acción',
        wish: 'desexa',
        question:'',
        create_date: 'Data de creación',
        create_date_start: 'Data de creación inicio',
        create_date_end: 'Data de creación fin',
        expiration_date:'Data de caducidade',
        generated_by: 'Xerado por',
        create_codes: 'Xerar códigos',
        news: 'Novos',
        create_new_codes: 'Indique o número de códigos a xerar e a data de caducidade',
        required_file_name: 'O nome do ficheiro é obrigatorio',
        required_expired_date: 'A data de caducidade é obrigatoria',
        required_number_codes: 'O número de códigos a xerar é obrigatorio',
        format_file:'Formato do ficheiro',
        export_codes:'Indique as datas de creación entre as que están comprendidas os códigos a exportar',
        export_creation_date:'A data de creación é obrigatoria',
        date_between:'Datas comprendidas entre',
        file_name:'Nome do ficheiro',
        no_codes_in_dates:'No hai códigos xerados nas datas indicadas',
        rules_creation_date_start: 'A data de creación inicio debe ser menor que a data de creacion fin',
        rules_creation_date_end: 'A data de creación fin debe ser maior que a data de creacion inicio',
        unsubscribe: 'Dar de baixa',
        unsubscribe_user: 'Dar de baixa usuario',
        unsubscribe_user_question:'Está seguro que desexa dar de baixa ao usuario ',
        unit:'Unidade',
        lesson: 'Lección',
        class_group_name: 'Nome do grupo',
        book_state_ready: 'Listo',
        book_state_pending: 'Pendente',
        book_state_completed:'Contido rematado',
        book_state_archived:'Archivado',
        total_subscriptions:'Total subscricións',
        total_class_groups:'Total grupos de clase',
        total_centers:'Total centros',
        delete_book:'Eliminar libro',
        cancel:'Cancelar',
        ok:'Aceptar',
        export:'Exportar',
        state:'Estado',
        permissions:'Permisos',
        users:'Usuarios',
        profile:'Perfil',
        question_delete_book:'Está seguro que desexa eliminar o libro?',
        new_codes:'Novos códigos',
        title_export_codes:'Exportar códigos',
        class_group_code:'Código do grupo de clase',
        select_book:'Escolla un libro',
        no_classes_created: 'Non ten clases creadas',
        no_classes_assigned: 'Non ten clases asignadas',
        delete_class:'Eliminar clase',
        delete_class_question: 'Seguro que desexa eliminar a clase ',
        edit_class:'Editar clase',
        edit:'Editar',
        class_code_copied:'Código da clase copiado',
        class_content:'Contido',
        class_activities:'Actividades',
        class_users:'Alumnado',
        show:'Mostrar',
        hide:'Oculta',
        end_date:'Data de fin',
        students:'Alumnado',
        enable_activity:'Habilitar actividade',
        homework_activity:'Deberes',
        see_activity_students:'Ver alumnos',
        duration:'Duración',
        score:'Puntuación',
        activity_user_list_table_title:'Alumnado que realizou a actividade ',
        required_login: 'O login é obrigatorio',
        valid_email:'O email debe ser válido',
        password_match:'Os contrasinais deben coincidir',
        add_book: 'Engadir libro',
        add_code_book:'Introduza o código do libro',
        subscription_completed_state:'Solicitude aceptada',
        subscription_cancelled_state:'Solicitude anulada',
        subscription_pending_state:'Solicitude pendente',
        no_students_realized_activity:'Ningún estudante realizou a actividade',
        user_deleted_class_group: 'Usuario eliminado do grupo de clase',
        delete:'Eliminar',
        assign_activity:'Asignar actividade',
        hour:'Hora',
        required_hour:'A hora é obrigatoria',
        required_date:'A data é obrigatoria',
        pending_activities:'Actividades pendentes',
        explore_activities:'Explorar actividades',
        activity:'Actividade',
        no_pending_activities:'Non ten ningunha actividade pendente',
        no_pending_books: 'Non hai libros pendentes',
        drop_file_click_select: 'Solte aquí o arquivo, ou faga click para seleccionalo.',
        book_with_code:'Libro con código de ',
        extension_support: 'Extensión soportada',
        please_select_image_file:'Por favor, seleccione unha imaxe',
        only_one_file:'So se pode subir un arquivo',
        duplicate:'Duplicar',
        duplicate_book:'Duplicar libro',
        question_duplicate_book: '¿Está seguro que desexa duplicar o libro ',
        required_unit_number: 'O número de unidade é obrigatorio',
        number:'Número',
        delete_unit:'Eliminar unidade',
        question_delete_unit:'Está seguro que desexa eliminar a unidade ',
        required_lesson_number:'O número da lección é obrigatorio',
        delete_lesson:'Eliminar lección',
        question_delete_lesson:'Está seguro que desexa eliminar a lección ',
        add_activity:'Engadir actividade',
        please_select_pdf_h5p_file:'Por favor, escolla un ficheiro pdf ou h5p ',
        activity_file:'Ficheiro da actividade',
        activity_image:'Imaxe da actividade',
        required_number:'O número é obrigatorio',
        edit_activity:'Editar actividade',
        delete_activity:'Eliminar actividade',
        question_delete_activity:'Seguro que desexa eliminar a actividade ',
        see_presentation_book:'Ver modo presentación',
        see_edition_book:'Ver modo edición',
        edit_book:'Editar libro',
        forgotten_password:'Esqueceu o contrasinal?',
        reset_password:'Introduza a súa dirección de correo ou login para recuperar o contrasinal',
        send:'Enviar',
        required_email_login:'O email ou login é obrigatorio',
        email_sent:'Email enviado',
        text_check_email:'O email foi enviado, por favor, revise o seu correo para cambiar o contrasinal',
        new_password:'Introduza o novo contrasinal',
        reset_password:'Restablecer contrasinal',
        password_changed:'Contrasinal cambiado',
        password_established:'Estableceuse o novo contrasinal',
        establish_login_password:'Estableza o se login e contrasinal',
        text_establish_login_password:'Por favor, introduza o seu login e contrasinal para acceder á aplicación de contido dixital',
        login_password_established:'Login e contrasinal establecidos',
        all:'Todo',
        no_content_visible:'Ainda non hai contido visible',
        enabled:'Habilitada',
        disabled:'Deshabilitado',
        edit_end_date:'Editar a data de fin',
        book_content:'Contido do libro',
        search_by_activity_description:'Búsqueda por descrición de actividade',
        search_by_lesson:'Búsqueda por lección',
        search_by_unit:'Búsqueda por unidade',
        search_by_user_fullname:'Búsqueda por nome completo de usuario',
        search_by_book_title:'Búsqueda por título do libro',
        search_by_course:'Búsqueda por curso',
        search_by_level:'Búsqueda por nivel',
        activity_not_found:'Non se atopou a actividade',
        save:'Gardar',
        no_books_subscriptions:'Non ten ningunha subscrición',
        finish:'Rematar',
        discard:'Descartar',
        new:'Novo',
        delete:'Eliminar',
        download:'Descargar',
        no_pending_books:'Non hai libros pendentes',
        image:'Imaxe',
        new_class_subscription:'Subscribirse a un grupo de clase',
        add_class_code:'Engade o código do grupo de clase',
        pending_subscription:'A solicitude para subscribirse ao grupo de clase aínda non foi aceptada',
        cancelled_subscription:'A solicitude para subscribirse ao grupo de clase foi cancelada',
        no_activities_assigned:'Non hai actividades asignadas',
        loading_activities:'Cargando actividades...',
        loading_activity:'Cargando actividade...',
        no_class_groups_created:'Non ten grupos de clase creados',
        score_out_of:'Puntuación sobre ',
        users_grades:'Cualificacións',
        avg_score:'Puntuación media',
        avg_homework_score:'Puntuación media de deberes',
        avg_not_assigned_act_score:'Puntuación media de actividades non asignadas',
        avg_duration:'Duracion media',
        activities_done: 'Actividades realizadas',
        activities_undone:'Actividades non realizadas',
        avg_class_group_score:'Puntuación media do grupo de clase ',
        avg_class_group_score_by_unit:'Puntuación media do grupo de clase na unidade',
        global_:'Global',
        no_activities_to_do:'O alumnado non ten actividades para facer',
        avg_duration_in_min: 'Duración media (minutos)',
        all_activities:'Todas as actividades',
        activities_done:'Actividades feitas',
        activities_undone:'Actividades sen facer',
        without_end_date: 'Sen data de entrega',
        authority:'Permisos',
        book_code:'Código do libro',
        required_class_group_code:'O código do grupo de clase é obrigatorio',
        required_book_code:'O código do libro e obrigatorio',
        add_class_group_code_and_book_code:'Engada o código do grupo de clase e o código do libro dixital',
        subscription_class_group:'Suscribirse a un grupo de clase',
        activities_with_same_h5p_content:'Non se pode eliminar o contido h5p, ten máis actividades asociadas',
        book_in_use:'En uso',
        search_by_book_state:'Búsqueda por estado',
        code_state_used:'Usado',
        code_state_new:'Novo'
    },
    "en-GB": {
        welcomeMsg: 'Welcome to your Vue.js application',
        digital_content:'Digital content',
        logged_as:'Logged as',
        home:'Home',
        books:'Books',
        statistics:'Statistics',
        classes:'Classes',
        class_groups:'Class groups',
        login:'Log in',
        logout:'Logout',
        create:'Create',
        passwd: 'Password',
        code:'Code',
        center:'Center',
        language:'Language',
        teacher:'Teacher',
        teachers:'Profesorado',
        student:'Student',
        students:'Students',
        admin:'Administrator',
        editor:'Editor',
        editors:'Editors',
        role:'Role',
        book:'Book',
        title:'Title',
        course:'Course',
        level:'Level',
        see_book:'See book',
        codes:'Codes',
        search:'Search',
        create_class_group:'Create class group',
        create_account: 'Create new account',
        forgot_passwd: 'Have you forgotten the password?',
        required_passwd: 'Password is required',
        required_email: 'Email is required',
        required_login: 'Login is required',
        register: 'Sign  up',
        repeat_passwd: 'Repeat password',
        required_code: 'Code is required',
        required_name: 'Name is required',
        required_last_name: 'Lastname is required',
        name: 'Name',
        last_name: 'LastName',
        birthdate: 'Birthdate',
        required_birthdate: 'Birthdate is required',
        required_language: 'Language is required',
        noBooks:'There is no book',
        add: 'Add',
        description: 'Description',
        required_title: 'Title is required',
        required_level: 'Level is required',
        required_course: 'Course is required',
        required_description: 'Description is required',
        tooltip_to_book_code_teacher: 'Students will only be able to see the content enabled by the teacher.',
        tooltip_to_book_code_student: 'The book will not have an associated teacher. Students will be able to see the entire contents of the book.',
        insert: 'Add',
        the: 'the',
        preposition:'the',
        text_new_class: 'Enter the name of the class linked to the book',
        required_class_name: 'Class name is required',
        create_new_class:'Create new class',
        required_select_book: 'Book is required',
        text_new_class_from_classes:'Add the book and the name of the class',
        user_management: 'Users management',
        content: 'Content',
        content_editor:'content editor',
        content_editors:'Content editors',
        rows_per_page:'Rows per page',
        register_date:'Register date',
        actions: 'Actions',
        action:'Action',
        wish: 'wish',
        question:'¿',
        create_date: 'Creation date',
        create_date_start: 'Init creation date',
        create_date_end: 'End creation date',
        expiration_date: 'Expiration date',
        generated_by: 'Generated by',
        create_codes: 'Create codes',
        news: 'News',
        create_new_codes: 'Indicate the number of codes to be generated and the expiration date.',
        required_file_name:'Filename is required',
        required_expired_date: 'Expiration date is required',
        required_number_codes: 'Number of codes is required',
        format_file:'Format file',
        export_codes: 'Indicate the dates of creation between which the codes to be exported are included',
        export_creation_date:'Creation date is required',
        date_between:'Dates between',
        file_name:'Filename',
        no_codes_in_dates: 'No codes created between the indicated dates',
        rules_creation_date_start: 'The start creation date must be less than the end creation date.',
        rules_creation_date_end: 'The end creation date must be greater than the start creation date.',
        unsubscribe: 'Unsubscribe',
        unsubscribe_user: 'Unsubscribe user',
        unsubscribe_user_question:'Are you sure you want to unsubscribe the user ',
        unit:'Unit',
        lesson:'Lesson',
        class_group_name: 'Group name',
        book_state_ready: 'Ready',
        book_state_pending: 'Pending',
        book_state_completed:'Completed content',
        book_state_archived:'Archived',
        total_subscriptions:'Total subscriptions',
        total_centers:'Total centers',
        total_class_groups:'Total grupos de clase',
        delete_book:'Delete book',
        cancel:'Cancel',
        ok:'OK',
        export:'Export',
        state:'State',
        permissions:'Permissions',
        users:'Users',
        profile:'Profile',
        question_delete_book:'Are you sure you want to delete the book?',
        new_codes:'New codes',
        title_export_codes:'Export codes',
        class_group_code:'Class group code',
        select_book:'Select a book',
        no_classes_created: 'No classes created yet',
        no_classes_assigned: 'No classes assigned yet',
        delete_class:'Delete class',
        delete_class_question: 'Are you sure you want to delete the class ',
        edit_class:'Edit class',
        edit:'Edit',
        class_code_copied:'Class code copied',
        class_content:'Content',
        class_activities:'Activities',
        class_users:'Students',
        show:'Show',
        hide:'Hide',
        end_date:'End date',
        students:'Students',
        enable_activity:'Enable activity',
        homework_activity:'Homework',
        see_activity_students:'See students',
        duration:'Duration',
        score:'Score',
        activity_user_list_table_title:'Students who did the activity ',
        required_login: 'Login is required',
        valid_email:'Email must be valid',
        password_match:'Passwords must be match',
        add_book: 'Add book',
        add_code_book:'Add the code of the book',
        subscription_completed_state:'Subscription approved',
        subscription_cancelled_state:'Subscription cancelled',
        subscription_pending_state:'Subscription pending',
        no_students_realized_activity:'No student has completed the activity',
        user_deleted_class_group: 'User deleted from the class group',
        delete:'Delete',
        assign_activity:'Assign activity',
        hour:'Time',
        required_hour:'Time is required',
        required_date:'End date is required',
        pending_activities:'Pending activities',
        explore_activities:'Explore activities',
        activity:'Activity',
        no_pending_activities:'There are any activity to do',
        no_pending_books: 'There are any pending book',
        drop_file_click_select: 'Drop your file here, or click to select it.',
        book_with_code:'Book with code of ',
        extension_support: 'Extension support',
        please_select_image_file:'Please, select an image file',
        only_one_file:'Only one file can be uploaded',
        duplicate:'Duplicate',
        duplicate_book:'Duplite book',
        question_duplicate_book: 'Are you sure you want to duplicate the book ',
        required_unit_number: 'Unit number is required',
        number:'Number',
        delete_unit:'Delete unit',
        question_delete_unit:'Are you sure you want to delete the unit ',
        required_lesson_number:'Lesson number is required',
        delete_lesson:'Delete lesson',
        question_delete_lesson:'Are you sure you want to delete the lesson ',
        add_activity:'Add activity',
        please_select_pdf_h5p_file:'Please, select a pdf or h5p file',
        activity_file:'Activity file',
        activity_image:'Activity image',
        required_number:'Number is required',
        edit_activity:'Edit activity',
        delete_activity:'Delete activity',
        question_delete_activity:'Are you sure you want to delete the activity ',
        see_presentation_book:'See presentation mode',
        see_edition_book:'See edition mode',
        edit_book:'Edit book',
        forgotten_password:'Have you forgotten yout password?',
        reset_password:'Enter your email or login to reset your password',
        send:'Send',
        required_email_login:'Email or login is required',
        email_sent:'Email sent',
        text_check_email:'The email has been sent, please, check your email to change the password',
        new_password:'Enter de new password',
        reset_password:'Reset password',
        password_changed:'Password changed',
        password_established:'The new password has been established',
        establish_login_password:'Establish your login and password',
        text_establish_login_password:'Please, enter your login and password to log in to the digital content application',
        login_password_established:'Login and password established',
        all:'All',
        no_content_visible:'No content visible yet',
        enabled:'Enabled',
        disabled:'Disabled',
        edit_end_date:'Edit end date',
        book_content:'Book content',
        search_by_activity_description:'Search by activity description',
        search_by_lesson:'Search by lesson',
        search_by_unit:'Search by unit',
        search_by_user_fullname:'Search by user full name',
        search_by_book_title:'Search by book title',
        search_by_course:'Search by course',
        search_by_level:'Search by level',
        activity_not_found:'Activity not found',
        save:'Save',
        no_books_subscriptions:'No subscriptions yet',
        finish:'Finish',
        discard:'Discard',
        new:'New',
        delete:'Delete',
        download:'Download',
        no_pending_books:'No pending books',
        image:'Image',
        new_class_subscription:'Class group subscription',
        add_class_code:'Add the code of the class group',
        pending_subscription:'The request to subscribe to the class group has not been accepted yet',
        cancelled_subscription:'The request to subscribe to the class group has been cancelled',
        no_activities_assigned:'No assigned activities',
        loading_activities:'Loading activities...',
        loading_activity:'Loading activity...',
        no_class_groups_created:'No class groups created',
        score_out_of:'Score out of  ',
        users_grades:'Grades',
        avg_score:'Average score',
        avg_homework_score:'Average homework score',
        avg_not_assigned_act_score:'Average not assigned activities score',
        avg_duration:'Average duration',
        activities_done: 'Activities done',
        activities_undone:'Activities undone',
        avg_class_group_score:'Average class group score ',
        avg_class_group_score_by_unit:'Average class group score in the unit',
        global_:'Global',
        no_activities_to_do:'Students do not have activities to do',
        avg_duration_in_min: 'Average duration (minutes)',
        minutes:'minutes',
        all_activities:'All activities',
        activities_done:'Activities done',
        activities_undone:'Activities undone',
        without_end_date:'Without end date',
        authority:'Authority',
        book_code:'Book code',
        required_class_group_code:'Class group code is required',
        required_book_code:'Book code is required',
        add_class_group_code_and_book_code:'Add the class group code and digital book code',
        subscription_class_group:'Subscribe to a class group',
        activities_with_same_h5p_content:'Cannot delete h5p content, there are more activities associated with it ',
        book_in_use:'In use',
        search_by_book_state:'Search by state',
        code_state_used:'Used',
        code_state_new:'New'
    },
}
const userLanguage = store.state.user.language;
function language(){
    if (userLanguage!="") {
        return userLanguage
        
    } else {
        return "es-ES"
    }
}
const i18n = new VueI18n({
    locale: language(),
    // locale:"es-ES",
    fallbackLocale: "gl-ES",
    messages
    
});  
    
export default i18n;
