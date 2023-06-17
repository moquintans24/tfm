--
-- PostgreSQL database dump
--

-- Dumped from database version 14.5
-- Dumped by pg_dump version 14.5

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: activities_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.activities_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: activity; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.activity (
    id bigint NOT NULL,
    code character varying(255),
    description character varying(255),
    idh5pfile character varying(255),
    image_path character varying(255),
    number integer,
    title character varying(255),
    typeh5p boolean,
    lesson_id bigint NOT NULL
);


--
-- Name: book; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.book (
    id bigint NOT NULL,
    course character varying(255),
    level integer,
    link_image character varying(255),
    state integer,
    title character varying(255)
);


--
-- Name: books_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.books_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: class_group; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.class_group (
    id bigint NOT NULL,
    code character varying(16),
    name character varying(255),
    lesson_id bigint,
    subscription_id bigint NOT NULL
);


--
-- Name: class_group_subscriptions; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.class_group_subscriptions (
    classes_id bigint NOT NULL,
    subscriptions_id bigint NOT NULL
);


--
-- Name: classes_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.classes_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: code; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.code (
    id bigint NOT NULL,
    code character varying(16),
    creation_date date,
    expiration_date date,
    state character varying(255),
    book_id bigint NOT NULL,
    generated_by_id bigint NOT NULL
);


--
-- Name: codes_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.codes_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: interaction_activity; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.interaction_activity (
    id bigint NOT NULL,
    duration time without time zone,
    homework_date timestamp without time zone,
    score double precision,
    activity_id bigint NOT NULL,
    subscription_id bigint NOT NULL,
    teacher_subscription_id bigint NOT NULL
);


--
-- Name: interation_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.interation_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: lesson; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.lesson (
    id bigint NOT NULL,
    name character varying(255),
    number integer,
    unit_id bigint NOT NULL
);


--
-- Name: lessons_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.lessons_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: post_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.post_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: subscriptions_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.subscriptions_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: tag_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.tag_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: the_subscription; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.the_subscription (
    id bigint NOT NULL,
    entry_date timestamp without time zone,
    state integer,
    class_group_id bigint,
    code_id bigint,
    user_id bigint NOT NULL
);


--
-- Name: the_subscription_activities; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.the_subscription_activities (
    subscriptions_id bigint NOT NULL,
    activities_id bigint NOT NULL
);


--
-- Name: the_user; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.the_user (
    id bigint NOT NULL,
    authority character varying(255),
    birth_date date,
    center character varying(255),
    email character varying(255),
    email_expiration_date date,
    language character varying(255),
    last_name character varying(255),
    link_image character varying(255),
    login character varying(255),
    name character varying(255),
    password character varying(255),
    registration_date timestamp without time zone,
    unsubscribe boolean,
    validation_string character varying(255)
);


--
-- Name: unit; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.unit (
    id bigint NOT NULL,
    name character varying(255),
    number integer,
    book_id bigint NOT NULL
);


--
-- Name: units_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.units_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: user_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.user_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Data for Name: activity; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.activity (id, code, description, idh5pfile, image_path, number, title, typeh5p, lesson_id) FROM stdin;
1	U1-L1-A1	Match	2244224162	B1-U1-L1-A1.jpg	1	Activity 1	t	1
2	U1-L1-A2	Write 'a' or 'an' 	3048435010	B1-U1-L1-A2.jpg	2	Activity 2	t	1
3	U1-L1-A3	Choose the correct answer	132891036	B1-U1-L1-A3.jpg	3	Activity 3	t	1
4	U1-L2-A1	Choose the correct answer	3313571172	B1-U1-L2-A1.jpg	1	Activity 1	t	2
5	U1-L2-A2	Write 'a' or 'an' 	1091330543	B1-U1-L2-A2.jpg	2	Activity 2	t	2
6	U1-L2-A3	Choose the correct answer	831178407	B1-U1-L2-A3.jpg	3	Activity 3	t	2
7	U1-L3-A1	Match	3899945293	B1-U1-L1-A1.jpg	1	Activity 1	t	3
8	U1-L3-A2	Write 'a' or 'an' 	3899945293	B1-U1-L1-A2.jpg	2	Activity 2	t	3
9	U1-L3-A3	Put the words in the correct order	3899945293	B1-U1-L1-A3.jpg	3	Activity 3	t	3
10	U2-L4-A1	Complete the questions and answers	3899945293	B1-U2-L4-A1.jpg	1	Activity 1	t	4
11	U2-L4-A2	Complete the sentences	3899945293	B1-U2-L4-A2.jpg	2	Activity 2	t	4
12	U2-L4-A3	Complete the questions	3899945293	B1-U2-L4-A3.jpg	3	Activity 3	t	4
13	U2-L5-A1	Complete the questions and answers	3899945293	B1-U2-L4-A1.jpg	1	Activity 1	t	5
14	U2-L5-A2	Complete the sentences	3899945293	B1-U2-L4-A2.jpg	2	Activity 2	t	5
15	U2-L6-A1	Complete the questions and answers	3899945293	B1-U2-L4-A1.jpg	1	Activity 1	t	6
16	U2-L6-A2	Complete the sentences	3899945293	B1-U2-L4-A2.jpg	2	Activity 2	t	6
17	U3-L7-A1	Match	3899945293	B1-U1-L1-A1.jpg	1	Activity 1	t	7
18	U3-L7-A2	Write 'a' or 'an' 	3899945293	B1-U1-L1-A2.jpg	2	Activity 2	t	7
19	U3-L7-A3	Put the words in the correct order	3899945293	B1-U1-L1-A3.jpg	3	Activity 3	t	7
20	U3-L8-A1	Match	3899945293	B1-U1-L1-A1.jpg	1	Activity 1	t	8
21	U3-L8-A2	Write 'a' or 'an' 	3899945293	B1-U1-L1-A2.jpg	2	Activity 2	t	8
22	U3-L8-A3	Put the words in the correct order	3899945293	B1-U1-L1-A3.jpg	3	Activity 3	t	8
23	U13-L9-A1	Match	3899945293	B1-U1-L1-A1.jpg	1	Activity 1	t	9
24	U3-L9-A2	Write 'a' or 'an' 	3899945293	B1-U1-L1-A2.jpg	2	Activity 2	t	9
25	U3-L9-A3	Put the words in the correct order	3899945293	B1-U1-L1-A3.jpg	3	Activity 3	t	9
26	U1-L1-A1	Atopa as palabras	3074377588	B2-U1-L1-A1.jpg	1	Actividade 1	t	16
27	U1-L1-A2	Verdadeiro/falso	128295353	B2-U1-L1-A2.jpg	2	Actividade 2	t	16
28	U1-L1-A3	Emparella os animais cos alimentos	4202725586	B2-U1-L1-A3.jpg	3	Actividade 3	t	16
29	U1-L2-A1	Actividade de memoria	2404824994	B2-U1-L2-A1.jpg	1	Actividade 1	t	17
30	U1-L2-A2	Atopa as palabras	4128076224	B2-U1-L2-A2.jpg	2	Actividade 2	t	17
31	U1-L3-A1	Indica a foita que se amosa nas imaxes	1422514150	B2-U1-L3-A1.jpg	1	Actividade 1	t	18
32	U1-L3-A2	Atopa as palabras	3074377588	B2-U1-L3-A2.jpg	2	Actividade 2	t	18
33	U1-L4-A1	Arrastra as palabras	2640495551	B2-U1-L4-A1.jpg	1	Actividade 1	t	19
34	U1-L4-A2	Atopa as palabras	3026234073	B2-U1-L4-A2.jpg	2	Actividade 2	t	19
35	U1-L4-A3	Emparella as imaxes	775404366	B2-U1-L4-A3.jpg	2	Actividade 3	t	19
36	U1-L5-A1	Emparella as imaxes	775404366	B2-U1-L1-A3.jpg	1	Actividade 1	t	20
37	U1-L5-A2	Atopa as palabras	3074377588	B2-U1-L1-A1.jpg	2	Actividade 2	t	20
38	U1-L5-A3	Verdadeiro/falso	128295353	B2-U1-L1-A2.jpg	3	Actividade 3	t	20
39	U1-L1-A1	Match	2244224162	B1-U1-L1-A1.jpg	1	Activity 1	t	28
40	U1-L1-A2	Write 'a' or 'an' 	3048435010	B1-U1-L1-A2.jpg	2	Activity 2	t	28
41	U1-L1-A3	Choose the correct answer	132891036	B1-U1-L1-A3.jpg	3	Activity 3	t	28
42	U1-L2-A1	Choose the correct answer	3313571172	B1-U1-L2-A1.jpg	1	Activity 1	t	29
43	U1-L2-A2	Write 'a' or 'an' 	1091330543	B1-U1-L2-A2.jpg	2	Activity 2	t	29
44	U1-L2-A3	Choose the correct answer	831178407	B1-U1-L2-A3.jpg	3	Activity 3	t	29
45	U1-L3-A1	Match	3899945293	B1-U1-L1-A1.jpg	1	Activity 1	t	30
46	U1-L3-A2	Write 'a' or 'an' 	3899945293	B1-U1-L1-A2.jpg	2	Activity 2	t	30
47	U1-L3-A3	Put the words in the correct order	3899945293	B1-U1-L1-A3.jpg	3	Activity 3	t	30
48	U2-L4-A1	Complete the questions and answers	3899945293	B1-U2-L4-A1.jpg	1	Activity 1	t	31
49	U2-L4-A2	Complete the sentences	3899945293	B1-U2-L4-A2.jpg	2	Activity 2	t	31
50	U2-L4-A3	Complete the questions	3899945293	B1-U2-L4-A3.jpg	3	Activity 3	t	31
51	U2-L5-A1	Complete the questions and answers	3899945293	B1-U2-L4-A1.jpg	1	Activity 1	t	32
52	U2-L5-A2	Complete the sentences	3899945293	B1-U2-L4-A2.jpg	2	Activity 2	t	32
53	U2-L6-A1	Complete the questions and answers	3899945293	B1-U2-L4-A1.jpg	1	Activity 1	t	33
54	U2-L6-A2	Complete the sentences	3899945293	B1-U2-L4-A2.jpg	2	Activity 2	t	33
55	U3-L7-A1	Match	3899945293	B1-U1-L1-A1.jpg	1	Activity 1	t	34
56	U3-L7-A2	Write 'a' or 'an' 	3899945293	B1-U1-L1-A2.jpg	2	Activity 2	t	34
57	U3-L7-A3	Put the words in the correct order	3899945293	B1-U1-L1-A3.jpg	3	Activity 3	t	34
58	U3-L8-A1	Match	3899945293	B1-U1-L1-A1.jpg	1	Activity 1	t	35
59	U3-L8-A2	Write 'a' or 'an' 	3899945293	B1-U1-L1-A2.jpg	2	Activity 2	t	35
60	U3-L8-A3	Put the words in the correct order	3899945293	B1-U1-L1-A3.jpg	3	Activity 3	t	35
61	U13-L9-A1	Match	3899945293	B1-U1-L1-A1.jpg	1	Activity 1	t	36
62	U3-L9-A2	Write 'a' or 'an' 	3899945293	B1-U1-L1-A2.jpg	2	Activity 2	t	36
63	U3-L9-A3	Put the words in the correct order	3899945293	B1-U1-L1-A3.jpg	3	Activity 3	t	36
102	U1-L1-A1	Atopa as palabras	3338021415	4c3f98ef-7630-4d4d-874e-8ebdf20ae14b.jpg	1	Actividade 1	t	52
103	U1-L1-A2	Verdadeiro ou falso	3305908053	ba81f429-0105-42f4-8d94-800835c290bc.jpg	2	Actividade 2	t	52
107	U1-L1-A1	Ordena los siguiente párrafos	2948034218	b9ac02d9-d924-4879-88a8-ce22dc66db9b.jpeg	1	Actividad 1	t	78
510	U1-L1-A1	Atopa as palabras	3338021415	4c3f98ef-7630-4d4d-874e-8ebdf20ae14b.jpg	1	Actividade 1	t	450
511	U1-L1-A2	Verdadeiro ou falso	3305908053	ba81f429-0105-42f4-8d94-800835c290bc.jpg	2	Actividade 2	t	450
\.


--
-- Data for Name: book; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.book (id, course, level, link_image, state, title) FROM stdin;
3	Primary	2	grammar_friends2.jpg	2	Grammar friends
5	Primaria	3	5lingua.jpg	0	Lingua 
406	Primaria	3	d59b8b3f-acd1-4060-be03-075b8c2d6cf6.jpg	3	Lingua Galega
2	Primaria	1	lingua1.png	2	Lingua
53	Primaria	3	53lengua1.jpg	0	Lengua
1	Primary	1	grammar_friends1.jpg	2	Grammar friends
\.


--
-- Data for Name: class_group; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.class_group (id, code, name, lesson_id, subscription_id) FROM stdin;
1	CL12wt5493p8fg54	English 1A	2	1
2	CL12wt5493pifg55	English 1B	2	1
3	CL17wt5238ppfg55	Lingua 1B	20	3
52	CLlmy1mjhqje42jv	Lingua 1C	\N	3
\.


--
-- Data for Name: class_group_subscriptions; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.class_group_subscriptions (classes_id, subscriptions_id) FROM stdin;
1	5
1	3
1	4
2	7
2	6
\.


--
-- Data for Name: code; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.code (id, code, creation_date, expiration_date, state, book_id, generated_by_id) FROM stdin;
1	kSxRZSxJnpZnFRRE	2022-09-15	2023-08-30	USED	1	1
2	YjNT9A8pqHheYBY2	2022-09-15	2023-08-30	USED	1	1
3	VUnUMoeGdj68U2R5	2022-07-31	2025-08-30	USED	1	1
4	4wwBkyMtgkSoAkoP	2022-07-31	2025-08-30	USED	1	1
5	YnLPN5chAPdvmA33	2022-07-31	2025-08-30	USED	1	1
6	6Avxmt2D7mdNEaZ8	2022-07-31	2025-08-30	USED	1	1
7	ZTKSPuHovNNoQhDU	2022-07-31	2025-08-30	USED	1	1
8	Ma6f2CxaT37YPoNY	2022-07-31	2025-08-30	NEW	1	1
9	RFGwVWkTv94TwC3C	2022-07-31	2025-08-30	NEW	1	1
10	3BKSuAXKCw8m8Gxy	2022-07-31	2025-08-30	NEW	1	1
11	QwwyMUsR3KBSfpzm	2022-07-31	2025-08-30	USED	3	1
12	onp5Fkikec22kMUc	2022-07-31	2025-08-30	NEW	3	1
13	1234Fkikec22kMUc	2022-07-31	2025-08-30	NEW	3	1
14	1234Fkikec22k123	2022-07-31	2025-08-30	NEW	3	1
15	1256Fkikec22k123	2022-07-31	2025-08-30	USED	2	1
16	1256Fkikec22k789	2022-07-31	2025-08-30	USED	2	1
17	2556Fkikec22k125	2022-07-31	2025-08-30	USED	2	1
18	1276Fsd58c22k128	2022-07-31	2025-08-30	USED	2	1
19	1256teikec22k136	2022-07-31	2025-08-30	USED	2	1
20	1476frikec22k189	2022-07-31	2025-08-30	USED	2	1
21	1288poikec22k175	2022-07-31	2025-08-30	USED	2	1
22	1288poik57w2k187	2022-07-31	2025-08-30	USED	2	1
23	1288plok57w2k187	2022-07-31	2025-08-30	USED	2	1
24	1288p77k57w2k187	2022-07-31	2025-08-30	NEW	2	1
\.


--
-- Data for Name: interaction_activity; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.interaction_activity (id, duration, homework_date, score, activity_id, subscription_id, teacher_subscription_id) FROM stdin;
2	00:08:01	2023-01-15 23:59:59	9	2	4	1
3	\N	\N	\N	3	4	1
5	\N	2023-01-15 23:59:59	\N	2	5	1
6	\N	\N	\N	3	5	1
8	00:17:02	2023-01-15 23:59:59	6	2	6	1
9	00:17:02	\N	8	3	6	1
10	00:17:02	2023-01-05 23:59:59	10	1	7	1
11	00:17:02	2023-01-15 23:59:59	9	2	7	1
12	00:17:02	\N	9	3	7	1
13	00:09:02	2023-01-05 23:59:59	10	1	8	1
14	00:10:25	2023-01-15 23:59:59	6	2	8	1
15	00:10:25	\N	9	3	8	1
16	00:08:01	2023-05-03 23:59:59	9	4	4	1
17	00:05:01	2023-05-03 23:59:59	9	5	4	1
18	\N	2023-05-03 23:59:59	\N	6	4	1
19	00:05:01	2023-05-03 23:59:59	8	4	5	1
20	\N	2023-05-03 23:59:59	\N	5	5	1
21	\N	2023-05-03 23:59:59	\N	6	5	1
22	00:08:01	2023-05-03 23:59:59	9	4	6	1
23	\N	2023-05-03 23:59:59	\N	5	6	1
24	\N	2023-05-03 23:59:59	\N	6	6	1
25	00:04:01	2023-05-03 23:59:59	7	4	7	1
26	\N	2023-05-03 23:59:59	\N	5	7	1
27	\N	2023-05-03 23:59:59	\N	6	7	1
28	00:04:01	2023-05-03 23:59:59	7	4	8	1
29	00:05:20	2023-05-03 23:59:59	9	5	8	1
30	\N	2023-05-03 23:59:59	\N	6	8	1
31	00:06:02	2023-05-03 23:59:59	9	10	4	1
32	00:05:09	2023-05-03 23:59:59	10	11	4	1
33	00:04:21	2023-05-03 23:59:59	7	10	5	1
34	\N	2023-05-03 23:59:59	\N	11	5	1
35	00:05:25	2023-05-03 23:59:59	9	10	6	1
36	\N	2023-05-03 23:59:59	\N	11	6	1
37	00:04:32	2023-05-03 23:59:59	7	10	7	1
38	\N	2023-05-03 23:59:59	\N	11	7	1
39	00:03:01	2023-05-03 23:59:59	6	10	8	1
40	00:08:20	2023-05-03 23:59:59	8	11	8	1
41	\N	2023-05-03 23:59:59	\N	26	12	3
42	00:05:09	2023-05-03 23:59:59	10	27	12	3
43	00:08:09	2023-05-03 23:59:59	10	28	12	3
44	00:09:09	2023-05-03 23:59:59	10	29	12	3
45	00:06:09	2023-05-03 23:59:59	10	30	12	3
46	00:08:29	2023-05-03 23:59:59	10	31	12	3
47	00:06:39	2023-05-03 23:59:59	10	32	12	3
48	00:07:19	2023-05-03 23:59:59	10	33	12	3
49	00:07:29	2023-05-03 23:59:59	10	34	12	3
50	00:08:49	2023-05-03 23:59:59	10	35	12	3
51	\N	2023-05-03 23:59:59	\N	36	12	3
52	00:04:09	2023-05-03 23:59:59	10	37	12	3
53	\N	2023-05-03 23:59:59	\N	38	12	3
54	00:03:01	2023-05-03 23:59:59	6	26	13	3
55	00:08:20	2023-05-03 23:59:59	8	27	13	3
56	00:05:21	2023-05-03 23:59:59	7	28	13	3
57	00:08:35	2023-05-03 23:59:59	7	29	13	3
58	00:08:01	2023-05-03 23:59:59	8	30	13	3
59	00:06:27	2023-05-03 23:59:59	8	31	13	3
60	00:06:01	2023-05-03 23:59:59	9	32	13	3
61	00:07:20	2023-05-03 23:59:59	10	33	13	3
62	00:08:11	2023-05-03 23:59:59	10	34	13	3
63	00:07:27	2023-05-03 23:59:59	5	35	13	3
64	\N	2023-05-03 23:59:59	\N	36	13	3
65	00:08:20	2023-05-03 23:59:59	8	37	13	3
66	\N	2023-05-03 23:59:59	\N	38	13	3
67	00:04:21	2023-05-03 23:59:59	7	26	14	3
68	00:04:21	2023-05-03 23:59:59	7	27	14	3
69	00:07:21	2023-05-03 23:59:59	9	28	14	3
70	00:08:21	2023-05-03 23:59:59	8	29	14	3
71	00:09:21	2023-05-03 23:59:59	8	30	14	3
72	00:05:21	2023-05-03 23:59:59	6	31	14	3
73	00:06:21	2023-05-03 23:59:59	7	32	14	3
74	\N	2023-05-03 23:59:59	\N	33	14	3
75	00:04:21	2023-05-03 23:59:59	5	34	14	3
76	\N	2023-05-03 23:59:59	\N	35	14	3
77	00:08:22	2023-05-03 23:59:59	8	36	14	3
78	\N	2023-05-03 23:59:59	\N	37	14	3
79	00:03:22	2023-05-03 23:59:59	8	38	14	3
80	00:05:25	2023-05-03 23:59:59	9	26	15	3
81	00:09:25	2023-05-03 23:59:59	9	27	15	3
82	00:05:55	2023-05-03 23:59:59	9	28	15	3
83	00:09:25	2023-05-03 23:59:59	9	29	15	3
84	00:08:45	2023-05-03 23:59:59	9	30	15	3
85	00:08:35	2023-05-03 23:59:59	9	31	15	3
86	00:06:35	2023-05-03 23:59:59	9	32	15	3
87	\N	2023-05-03 23:59:59	\N	33	15	3
88	00:06:25	2023-05-03 23:59:59	9	34	15	3
89	\N	2023-05-03 23:59:59	\N	35	15	3
90	00:08:25	2023-05-03 23:59:59	9	36	15	3
91	00:09:25	2023-05-03 23:59:59	9	37	15	3
92	\N	2023-05-03 23:59:59	\N	38	15	3
93	00:04:25	2023-05-03 23:59:59	7	26	16	3
94	\N	2023-05-03 23:59:59	\N	27	16	3
95	00:06:06	2023-05-03 23:59:59	8	28	16	3
96	00:05:32	2023-05-03 23:59:59	9	29	16	3
97	00:04:22	2023-05-03 23:59:59	6	30	16	3
98	00:08:32	2023-05-03 23:59:59	9	31	16	3
99	00:07:32	2023-05-03 23:59:59	9	32	16	3
100	\N	2023-05-03 23:59:59	\N	33	16	3
101	00:04:32	2023-05-03 23:59:59	8	34	16	3
102	\N	2023-05-03 23:59:59	\N	35	16	3
103	\N	2023-05-03 23:59:59	\N	36	16	3
104	\N	2023-05-03 23:59:59	\N	37	16	3
105	\N	2023-05-03 23:59:59	\N	38	16	3
106	00:09:32	2023-05-03 23:59:59	7	26	17	3
107	00:08:32	2023-05-03 23:59:59	8	27	17	3
108	00:05:32	2023-05-03 23:59:59	5	28	17	3
109	\N	2023-05-03 23:59:59	\N	29	17	3
110	00:04:32	2023-05-03 23:59:59	4	30	17	3
111	00:09:22	2023-05-03 23:59:59	9	31	17	3
112	00:09:26	2023-05-03 23:59:59	8	32	17	3
113	\N	2023-05-03 23:59:59	\N	33	17	3
114	00:04:36	2023-05-03 23:59:59	7	34	17	3
115	\N	2023-05-03 23:59:59	\N	35	17	3
116	\N	2023-05-03 23:59:59	\N	36	17	3
117	\N	2023-05-03 23:59:59	\N	37	17	3
118	\N	2023-05-03 23:59:59	\N	38	17	3
152	\N	2023-01-25 23:59:00	\N	3	4	1
153	\N	2023-01-25 23:59:00	\N	3	5	1
154	\N	2023-01-25 23:59:00	\N	3	6	1
1	00:10:00	2023-01-25 23:59:55	8	1	4	1
4	00:12:26	2023-01-25 23:59:55	6	1	5	1
7	00:20:58	2023-01-25 23:59:55	9	1	6	1
\.


--
-- Data for Name: lesson; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.lesson (id, name, number, unit_id) FROM stdin;
1	School things	1	1
2	My toys	2	1
3	My body	3	1
4	Jobs	4	2
5	At the park	5	2
6	My family	6	2
7	Clothes	7	3
8	My house	8	3
9	My lunch	9	3
10	My friends	10	4
11	Zoo trip	11	4
12	Family meal	12	4
13	My room	13	5
14	Abilities	14	5
15	At the beach	15	5
16	Imos á escola	1	6
17	Facemos ximnasia	2	6
18	Comemos con xeito	3	6
19	O mundo	4	6
20	Animais que viven con nos	5	7
21	Unha ollada ao redor	6	7
22	Lixo ningún	7	7
23	Auga necesaria	8	7
24	Aproveitar con xeito	9	8
25	Maquinarias	10	8
26	A escena	11	8
27	Os oceanos	12	8
28	At school	1	9
29	My feelings	2	9
30	Outdoor toys	3	9
31	Luch at the park	4	9
32	Lessons	5	9
33	After school	6	9
34	Presents	7	9
35	Every day	8	10
36	Places	9	10
37	The weather	10	10
38	Clothes	11	10
39	At the wedding	12	10
40	On the farm	13	10
41	The school play	14	10
42	School open day	15	10
52	Ler e escribir	1	52
53	Cóntame un conto	2	52
54	Merendamos	3	52
55	Crea cómics	4	52
56	Reláxate no bosque	5	53
57	Faite poeta	6	53
58	Infórmate e recicla	7	53
59	Sube  ao escenario	8	53
60	Coñece a música galega!	9	54
61	Cambia de aires	10	54
62	Respecta os animais	11	54
63	A xogar!	12	54
78	¡Comenzamos en orden	1	62
79	¿Qué te hace feliz?	2	62
450	Ler e escribir	1	414
451	Cóntame un conto	2	414
452	Merendamos	3	414
453	Crea cómics	4	414
454	Reláxate no bosque	5	415
455	Faite poeta	6	415
456	Infórmate e recicla	7	415
457	Sube  ao escenario	8	415
458	Coñece a música galega!	9	416
459	Cambia de aires	10	416
460	Respecta os animais	11	416
461	A xogar!	12	416
\.


--
-- Data for Name: the_subscription; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.the_subscription (id, entry_date, state, class_group_id, code_id, user_id) FROM stdin;
1	2022-12-03 16:12:39	0	\N	3	4
2	2022-12-03 16:12:39	0	\N	11	4
3	2022-12-03 16:12:39	0	\N	15	5
4	2022-12-03 16:12:39	0	1	2	6
5	2022-12-03 16:12:39	0	1	1	7
6	2022-12-03 16:12:39	0	1	4	8
7	2022-12-03 16:12:39	0	2	5	9
8	2022-12-03 16:12:39	0	2	6	10
9	2022-12-03 16:12:39	2	1	10	11
10	2022-12-03 16:12:39	2	1	9	12
11	2022-12-03 16:12:39	\N	\N	7	31
12	2022-12-03 16:12:39	0	3	16	36
13	2022-12-03 16:12:39	0	3	17	37
14	2022-12-03 16:12:39	0	3	18	34
15	2022-12-03 16:12:39	0	3	19	35
16	2022-12-03 16:12:39	0	3	20	33
17	2022-12-03 16:12:39	0	3	21	38
18	2022-12-03 16:12:39	2	3	22	32
19	2022-12-03 16:12:39	\N	\N	23	39
52	2023-05-17 11:04:24.050362	2	3	24	6
102	2023-05-21 19:54:30.782826	2	1	8	33
\.


--
-- Data for Name: the_subscription_activities; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.the_subscription_activities (subscriptions_id, activities_id) FROM stdin;
\.


--
-- Data for Name: the_user; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.the_user (id, authority, birth_date, center, email, email_expiration_date, language, last_name, link_image, login, name, password, registration_date, unsubscribe, validation_string) FROM stdin;
1	ADMIN	\N	\N	\N	\N	\N	\N	\N	adrian	\N	$2a$10$wAZYtID1NsQ8z2gUuh4Aiulygjc8BUQOWEF/o4E61JXo7C3PPkzCa	\N	f	\N
2	EDITOR	1987-10-10	\N	elsa.perez@gmail.com	\N	es-ES	Pérez	2elsa.jpg	elsa	Elsa	$2a$10$YBHMw1s3LBxlc6eNjtHLIeAF5oTE4PDSh.pY7rom48C.L3NbxAKpS	2023-05-01 14:03:36.950137	f	\N
3	EDITOR	1987-10-10	\N	juan.perez@gmail.com	\N	gl-ES	Pérez	3j.perez.jpg	j.perez	Juan	$2a$10$PJzvrnbdqdWbQX3XR/FpaO9y0ZMBiv13CbE2oo5hiC3GiPqYfxw3y	2023-05-01 14:03:37.019927	f	\N
5	TEACHER	1983-11-10	centro2	m.vazquez@gmail.com	\N	gl-ES	Vázquez	5maria.png	maria	María	$2a$10$X58Zy3d6XGetTKT1kwF6k.fZoVm06j5Tcw9M/JLAMrBHYWmj0DgVi	2023-05-01 14:03:37.172946	f	\N
6	USER	2016-10-10	centro2	l.gonzalez@gmail.com	\N	en-GB	González	6lucas.png	lucas	Lucas	$2a$10$Q72QjJ2/IlW8DhXhUd/BwOxbjcCAfQaTdj.UKU51p63r8szkit/Z.	2023-05-01 14:03:37.245036	f	\N
7	USER	2016-10-10	centro2	j.estevez@gmail.com	\N	en-GB	Estévez	7juan.png	juan	Juan	$2a$10$LlRt8U8JbkurdWHhbc75TO6FIfOAGVREPUHf4IgcDRCadN7qwkWKe	2023-05-01 14:03:37.313036	f	\N
8	USER	2016-10-10	centro2	m.abalo@gmail.com	\N	en-GB	Abalo	8martaab.png	martaab	Marta	$2a$10$ZknfJ1FMCQIpOvO6iipmBeRHv7.W1BozXQcP82fd1zKzUYQ7ZsOI2	2023-05-01 14:03:37.386945	f	\N
9	USER	2016-10-10	centro2	fran.gonzalez@gmail.com	\N	en-GB	González	9fran.png	fran	Fran	$2a$10$TEgEFhRD1/x27HhOAJf2vOoqUyuVbotBDfai7JoLkhrtZDbcb2iSK	2023-05-01 14:03:37.467176	f	\N
10	USER	2016-10-10	centro2	c.estevez@gmail.com	\N	en-GB	Estévez	10cesar.png	cesar	Cesar	$2a$10$c.emWep7o.9YMVnxzwbGH.8W2CsTKw6FYtoIvnv8Xf16b5x29c4uq	2023-05-01 14:03:37.546087	f	\N
11	USER	2016-10-10	centro2	ana.perez10@gmail.com	\N	en-GB	Pérez	11anap.png	anap	Ana	$2a$10$AVx4biNuU6acBcYAfOEdnuAsCwCEz9HD8MP9wRa44TWQckBBRGe8K	2023-05-01 14:03:37.612085	f	\N
12	USER	2016-10-10	centro2	laura.castro@gmail.com	\N	en-GB	Castro	12laura.png	laura	Laura	$2a$10$ZQV6ZyEfszqHENO5IkjKweRSvCQfEvIrzkq9Tuig5RDizhaI1LZTK	2023-05-01 14:03:37.682178	f	\N
13	EDITOR	1987-10-10	\N	lucas.perez@gmail.com	\N	es-ES	Pérez	13lucas_p.png	lucas_p	Lucas	$2a$10$AJMYClXxMjsefv50BkW4I.En9AEshaS6BKDarDssLeH5sSVSNHnMy	2023-05-01 14:03:37.74809	f	\N
14	EDITOR	1987-10-10	\N	laura.gonzalez@gmail.com	\N	en-GB	González	14laurag.jpeg	laurag	Laura	$2a$10$ADX1jbt26afYMwtPs56NIO05IR5jZ7t7BZEsqkiThxXi5KN6ZMP9a	2023-05-01 14:03:37.818168	f	\N
15	EDITOR	1987-10-10	\N	elena.estevez@gmail.com	\N	gl-ES	Estévez	15elena.png	elena	Elena	$2a$10$NapX3EuPwkyCm7QduNLGq.MQkjX8edx/83D/LtgCngWn/Rm8JqGgS	2023-05-01 14:03:37.886189	f	\N
16	EDITOR	1987-10-10	\N	alba.r@gmail.com	\N	en-GB	Ruiz	16alba.jpeg	alba	Alba	$2a$10$9wUI0WggA4Vbyc7pY0iRXOujX/maptQ6Fz5Nd4GyH0wbAnFy97Oty	2023-05-01 14:03:37.954169	f	\N
17	EDITOR	1987-10-10	\N	jorge.rodgz@gmail.com	\N	en-GB	Rodriguez	17jorge.jpg	jorge	Jorge	$2a$10$nDtXqCg4ce2.TsfjqoIxx.89uRcqJ66JtkxSGQwQHaqW8ZwWjRvlq	2023-05-01 14:03:38.02557	f	\N
18	EDITOR	1987-10-10	\N	casal.diego@gmail.com	\N	es-ES	Casal	18diego.png	diego	Diego	$2a$10$h/aUqeAIoFBHtQtESdmUH.M.VzGs4ecDlXrVN8/C07VXJ7CIcWSCK	2023-05-01 14:03:38.092583	f	\N
19	EDITOR	1987-10-10	\N	hernandez.ana@gmail.com	\N	gl-ES	Hernández	19anah.jpg	anah	Ana	$2a$10$6zbAA.7R8YvEw5dSPGyLuuQ1xVksacCSNQ9C8i5HcmgXTrtLRUYE.	2023-05-01 14:03:38.159568	f	\N
20	EDITOR	1987-10-10	\N	f_marcos@gmail.com	\N	en-GB	Fernández	20marcos_f.jpeg	marcos_f	Marcos	$2a$10$wLbtEDFTlxtLl.EsjetmGOSUeMso6We99/f6SQgUZCZYfxVZSnT.y	2023-05-01 14:03:38.22557	f	\N
21	EDITOR	1987-10-10	\N	manuel_ortiz@gmail.com	\N	en-GB	Ortiz	21manuel_o.png	manuel_o	Manuel	$2a$10$ielN8BTooKSPgDUNqbaNC.UBIHT9dXv8J.kgWZ9GUnqMqYScd9h2.	2023-05-01 14:03:38.288463	f	\N
22	EDITOR	1987-10-10	\N	lucia.ab@gmail.com	\N	gl-ES	Abalo	22lucia_ab.png	lucia_ab	Lucia	$2a$10$dU8acerR3R.UmM3CS1rhkOnnl57X6Xz6mzl5iYBlpjP/Phh.ZmJqS	2023-05-01 14:03:38.35846	f	\N
23	TEACHER	1987-10-10	centro2	a.gonzalez@gmail.com	\N	en-GB	González	23alfredo.png	alfredo	Alfredo	$2a$10$GYgYuw3fLIPzYTa7AtsoyOBl2mTj0QozanXTmQWaUH4HA4Srbz64S	2023-05-01 14:03:38.426459	f	\N
24	TEACHER	1983-11-10	centro2	l.vazquez@gmail.com	\N	es-ES	Vázquez	24lucasvz.png	lucasvz	Lucas	$2a$10$RJ8WZkS/ugStntvZeAiwt.xYQXZsHYPw9k1bi3iF56hCFlUl3Nu7S	2023-05-01 14:03:38.504572	f	\N
25	TEACHER	1987-10-10	centro3	g.gonzalez@gmail.com	\N	en-GB	González	25gonzalo.png	gonzalo	Gonzalo	$2a$10$8E1iUZdi3Xd/Uebdq7sZh.kAUHjr25PCLwzNSbr.Igu9OHsgtPRSK	2023-05-01 14:03:38.569659	f	\N
26	TEACHER	1983-11-10	centro3	m.estevez@gmail.com	\N	es-ES	Estévez	26maria_24.png	maria_24	María	$2a$10$OTdjc0iXTOCCaFrAzVUDIu1P8xR61.8dSUStN8KGsrSaJTrqmunWa	2023-05-01 14:03:38.63366	f	\N
27	TEACHER	1987-10-10	centro3	b.martinez@gmail.com	\N	en-GB	Martínez	27beatriz.png	beatriz	Bea	$2a$10$4EGQqsN7J4UV1BUY.SgcOum8huCEoEGwvG.q3MQEKCP2Kohxz7eUy	2023-05-01 14:03:38.704574	f	\N
28	TEACHER	1983-11-10	centro3	eva.lorenzo@gmail.com	\N	es-ES	Lorenzo	28eva_l.png	eva_l	Eva	$2a$10$L5b3ZxjzuSKGGzUDSG8E9OHVwYJu7riy6NUpRiMkVEcxjywzBeWkS	2023-05-01 14:03:38.778576	f	\N
29	TEACHER	1987-10-10	centro3	ivan.s@gmail.com	\N	en-GB	Sánchez	29ivan.png	ivan	Iván	$2a$10$v4TUuD6Zpr51H7kHMUsrDenm/uIWl2AOlTIxJN5pgNOfYJ8bVt6Oi	2023-05-01 14:03:38.851666	f	\N
30	TEACHER	1983-11-10	centro2	a.vazquez@gmail.com	\N	es-ES	Vázquez	30antonia.png	antonia	Antonia	$2a$10$K.5vBHQALjA0ke6UKt1dcuUxd5F2Jpq6srj5ImP9w66vz2W7k0JCq	2023-05-01 14:03:38.919572	f	\N
31	USER	2016-10-10	centro2	maria.gonzalez@gmail.com	\N	en-GB	González	31mariag.png	mariag	María	$2a$10$GjK.NzV7N.NTou1joe7F1.SwqI1v09xqtjLhz/07Uq/qvdIXk3Zfa	2023-05-01 14:03:38.987075	f	\N
32	USER	2016-10-10	centro2	a.gonzalezv@gmail.com	\N	gl-ES	González Varela	32angela.png	angela	Angela	$2a$10$8IGeoqCBEqH.oZgHyLdM6OJeEzA.P2dyz46vDq.Wkw7Ci2sP7a8ii	2023-05-01 14:03:39.056098	f	\N
33	USER	2016-10-10	centro2	p.aestevez@gmail.com	\N	gl-ES	Abalo Estévez	33pedro.png	pedro	Pedro	$2a$10$MJZ7eA/f1qQpMMjbPPkPVe8UGFL094xre.Co34y43bdNYH1Dp2YaW	2023-05-01 14:03:39.124583	f	\N
34	USER	2016-10-10	centro2	m.fabalo@gmail.com	\N	gl-ES	Fernández Abalo	34martafa.png	martafa	Marta	$2a$10$hpVSjXZgLKS1BznSIbxPW.SRPPAtEx6XPM.HbovpgMvTnCh6pST7.	2023-05-01 14:03:39.195606	f	\N
35	USER	2016-10-10	centro2	hugo.gonzalezbl@gmail.com	\N	gl-ES	González Blanco	35hugo.png	hugo	Hugo	$2a$10$dDVP2SNTPqrlMcEV4KO1oOAzOFDGL6AiJapZAFpyqD6IG9Ey/ToPy	2023-05-01 14:03:39.264602	f	\N
36	USER	2016-10-10	centro2	ap.estevez@gmail.com	\N	gl-ES	Estévez Abalo	36anapaula.png	anapaula	Ana Paula	$2a$10$Nfu4sNFRaC2XoeE4QM9treHzGH0OHqUEWJ89oHPBX4Lc8AteQPl4e	2023-05-01 14:03:39.339606	f	\N
37	USER	2016-10-10	centro2	ana.perezagudo@gmail.com	\N	gl-ES	Pérez Agudo	37anamaria.png	anamaria	Ana Maria	$2a$10$TGQ/SN.OKtwbCJ2ujCNUx.hw.zTDuHk.i56Uko2k/beVA748LJdqa	2023-05-01 14:03:39.406623	f	\N
38	USER	2016-10-10	centro2	laura.cblanco@gmail.com	\N	gl-ES	Casas Blanco	38lauracasas.png	lauracasas	Laura	$2a$10$WEIP37mkvPWh.9Tg6RXsTuzMewpwT7KaLFZvf7E97FAGcdkBLJP7i	2023-05-01 14:03:39.482796	f	\N
39	USER	2016-10-10	centro2	alberto_fgonzalez@gmail.com	\N	gl-ES	Fernández Gonzalez	39albertof.png	albertof	Alberto	$2a$10$ZJ5S4DXLKHGWEVSNFUUEJuOENYcMRw9e5xpdqBKx9whuCbPDpQxS6	2023-05-01 14:03:39.549708	f	\N
4	TEACHER	1987-10-10	centro2	m.gonzalez@gmail.com	\N	en-GB	González	84372cf6-211b-46db-a4b4-1fa679f6edd8.png	marta	Marta	$2a$10$BUBBTJsI0m5eHe8WzuyXheHc9WIy6PMMyuGRrhv/VRLtjzf1KBxUK	2023-05-01 14:03:37.102947	f	\N
52	EDITOR	\N	\N	mariaocampoquintans@gmail.com	2023-05-21	es-ES	Ocampo	default_user_image.jpg	mocampo	Maria	$2a$10$jFLutesfUS.XQSjWh3yAjOA40lA6EX6YfHbmbfg7qnqZKbePTyWJ6	2023-05-19 18:31:44.688774	t	8a9ae963d794af399e510d426d521dd5646fb311405b0f2f93215d018660d7ef
\.


--
-- Data for Name: unit; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.unit (id, name, number, book_id) FROM stdin;
1	Hello	1	1
2	Review	2	1
3	Review	3	1
4	Review	4	1
5	Review	5	1
6	Unidade	1	2
7	Unidade	2	2
8	Unidade	3	2
9	About us	1	3
10	Review	2	3
11	Review	3	3
12	Review	4	3
13	Review	5	3
52	Unidade	1	5
53	Unidade	2	5
54	Unidade	3	5
62	Unidad	1	53
414	Unidade	1	406
415	Unidade	2	406
416	Unidade	3	406
\.


--
-- Name: activities_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.activities_seq', 551, true);


--
-- Name: books_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.books_seq', 451, true);


--
-- Name: classes_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.classes_seq', 101, true);


--
-- Name: codes_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.codes_seq', 51, true);


--
-- Name: interation_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.interation_seq', 201, true);


--
-- Name: lessons_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.lessons_seq', 501, true);


--
-- Name: post_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.post_seq', 1, false);


--
-- Name: subscriptions_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.subscriptions_seq', 151, true);


--
-- Name: tag_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.tag_seq', 1, false);


--
-- Name: units_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.units_seq', 451, true);


--
-- Name: user_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.user_seq', 101, true);


--
-- Name: activity activity_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.activity
    ADD CONSTRAINT activity_pkey PRIMARY KEY (id);


--
-- Name: book book_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.book
    ADD CONSTRAINT book_pkey PRIMARY KEY (id);


--
-- Name: class_group class_group_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.class_group
    ADD CONSTRAINT class_group_pkey PRIMARY KEY (id);


--
-- Name: class_group_subscriptions class_group_subscriptions_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.class_group_subscriptions
    ADD CONSTRAINT class_group_subscriptions_pkey PRIMARY KEY (classes_id, subscriptions_id);


--
-- Name: code code_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.code
    ADD CONSTRAINT code_pkey PRIMARY KEY (id);


--
-- Name: interaction_activity interaction_activity_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.interaction_activity
    ADD CONSTRAINT interaction_activity_pkey PRIMARY KEY (id);


--
-- Name: lesson lesson_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.lesson
    ADD CONSTRAINT lesson_pkey PRIMARY KEY (id);


--
-- Name: the_subscription_activities the_subscription_activities_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.the_subscription_activities
    ADD CONSTRAINT the_subscription_activities_pkey PRIMARY KEY (subscriptions_id, activities_id);


--
-- Name: the_subscription the_subscription_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.the_subscription
    ADD CONSTRAINT the_subscription_pkey PRIMARY KEY (id);


--
-- Name: the_user the_user_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.the_user
    ADD CONSTRAINT the_user_pkey PRIMARY KEY (id);


--
-- Name: the_user uk_a9q39ot5spvwaa73jphf5rsf1; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.the_user
    ADD CONSTRAINT uk_a9q39ot5spvwaa73jphf5rsf1 UNIQUE (login);


--
-- Name: code uk_de1dtuynd2biba9l1snao3bt5; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.code
    ADD CONSTRAINT uk_de1dtuynd2biba9l1snao3bt5 UNIQUE (code);


--
-- Name: the_user uk_fqia11pv4g2yuul69b4ff7sf1; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.the_user
    ADD CONSTRAINT uk_fqia11pv4g2yuul69b4ff7sf1 UNIQUE (email);


--
-- Name: unit unit_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.unit
    ADD CONSTRAINT unit_pkey PRIMARY KEY (id);


--
-- Name: activity fk57s17leqgitd4ptbuj6b9rtj2; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.activity
    ADD CONSTRAINT fk57s17leqgitd4ptbuj6b9rtj2 FOREIGN KEY (lesson_id) REFERENCES public.lesson(id);


--
-- Name: the_subscription fk86k1kgwkn3te58ey6mhstqepk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.the_subscription
    ADD CONSTRAINT fk86k1kgwkn3te58ey6mhstqepk FOREIGN KEY (user_id) REFERENCES public.the_user(id);


--
-- Name: interaction_activity fk9eem28gv0udbu4fmiu6l7wivu; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.interaction_activity
    ADD CONSTRAINT fk9eem28gv0udbu4fmiu6l7wivu FOREIGN KEY (teacher_subscription_id) REFERENCES public.the_subscription(id);


--
-- Name: interaction_activity fka3kypn0kkhk2botwirs1s20dm; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.interaction_activity
    ADD CONSTRAINT fka3kypn0kkhk2botwirs1s20dm FOREIGN KEY (subscription_id) REFERENCES public.the_subscription(id);


--
-- Name: interaction_activity fka3ywnqhg9tcrqqvp53s8inkh3; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.interaction_activity
    ADD CONSTRAINT fka3ywnqhg9tcrqqvp53s8inkh3 FOREIGN KEY (activity_id) REFERENCES public.activity(id);


--
-- Name: the_subscription fkas1kxdoslqx2sr5k348gmf0ur; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.the_subscription
    ADD CONSTRAINT fkas1kxdoslqx2sr5k348gmf0ur FOREIGN KEY (code_id) REFERENCES public.code(id);


--
-- Name: class_group fkdfctxh45quytmceqan1hs0d17; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.class_group
    ADD CONSTRAINT fkdfctxh45quytmceqan1hs0d17 FOREIGN KEY (subscription_id) REFERENCES public.the_subscription(id);


--
-- Name: unit fke1m32k3hxu98gp428e0yru4p9; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.unit
    ADD CONSTRAINT fke1m32k3hxu98gp428e0yru4p9 FOREIGN KEY (book_id) REFERENCES public.book(id);


--
-- Name: class_group fkkqlbns1xye0akcispp16fo9sb; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.class_group
    ADD CONSTRAINT fkkqlbns1xye0akcispp16fo9sb FOREIGN KEY (lesson_id) REFERENCES public.lesson(id);


--
-- Name: code fko54lwusdrek1g6otdmtob9hgg; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.code
    ADD CONSTRAINT fko54lwusdrek1g6otdmtob9hgg FOREIGN KEY (book_id) REFERENCES public.book(id);


--
-- Name: code fko6ggdmip6s1sm47fhghou5y9k; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.code
    ADD CONSTRAINT fko6ggdmip6s1sm47fhghou5y9k FOREIGN KEY (generated_by_id) REFERENCES public.the_user(id);


--
-- Name: lesson fkoha1pbgh7okicxvwjdbgcyoe8; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.lesson
    ADD CONSTRAINT fkoha1pbgh7okicxvwjdbgcyoe8 FOREIGN KEY (unit_id) REFERENCES public.unit(id);


--
-- Name: the_subscription fktnsd5g1kb1dlxsdh090x3i7nh; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.the_subscription
    ADD CONSTRAINT fktnsd5g1kb1dlxsdh090x3i7nh FOREIGN KEY (class_group_id) REFERENCES public.class_group(id);


--
-- PostgreSQL database dump complete
--

