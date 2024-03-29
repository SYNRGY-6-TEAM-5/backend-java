--
-- PostgreSQL database dump
--

-- Dumped from database version 16.0
-- Dumped by pg_dump version 16.0

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

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: image; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.image (
    image_id uuid NOT NULL,
    name_image character varying(255),
    url_image character varying(255)
);


ALTER TABLE public.image OWNER TO postgres;

--
-- Name: otp; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.otp (
    uuid uuid NOT NULL,
    email character varying(255),
    created_time timestamp(6) without time zone,
    otp_code character varying(255),
    exp_time timestamp(6) without time zone
);


ALTER TABLE public.otp OWNER TO postgres;

--
-- Name: otp_forgot_password; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.otp_forgot_password (
    uuid uuid NOT NULL,
    email character varying(255),
    exp_time timestamp(6) without time zone,
    created_time timestamp(6) without time zone,
    otp_code character varying(255)
);


ALTER TABLE public.otp_forgot_password OWNER TO postgres;

--
-- Name: otp_register; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.otp_register (
    uuid uuid NOT NULL,
    email character varying(255),
    exp_time timestamp(6) without time zone,
    fullname character varying(255),
    created_time timestamp(6) without time zone,
    otp_code character varying(255),
    password character varying(255)
);


ALTER TABLE public.otp_register OWNER TO postgres;

--
-- Name: role; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.role (
    role_id uuid NOT NULL,
    role_name character varying(255),
    CONSTRAINT role_role_name_check CHECK (((role_name)::text = ANY ((ARRAY['USER'::character varying, 'ADMIN'::character varying])::text[])))
);


ALTER TABLE public.role OWNER TO postgres;

--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    is_active boolean,
    created_at timestamp(6) without time zone,
    role_id uuid,
    user_id uuid NOT NULL,
    email_address character varying(255),
    fullname character varying(255),
    password character varying(255),
    image_id uuid,
    bitrh_date timestamp(6) without time zone,
    last_modified timestamp(6) without time zone,
    phone_num bigint
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Data for Name: image; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.image (image_id, name_image, url_image) FROM stdin;
f7437fed-aab9-42a5-befe-fa6be60b1e9f	a	https://res.cloudinary.com/drpx4fqwg/image/upload/v1/folder_1/g2axmvfdaxeksjphdegz
2ba2d3c2-dec6-42f4-ba7c-42d2034bda76	a	https://res.cloudinary.com/drpx4fqwg/image/upload/v1/folder_1/e9flumysux11o1mcvzeg
4208195d-780c-4033-91cc-74e55fad3e7e	a	https://res.cloudinary.com/drpx4fqwg/image/upload/v1/user-images/vhtu9hlwwoyuedvzvbob
4c184c45-b585-4bec-b2ef-2dd146fd4c47	a	https://res.cloudinary.com/drpx4fqwg/image/upload/v1/user-images/xhdhrrdgbcmfcaank6el
a945cbdd-9396-4e3a-9a73-b8e33e856ab4	a	https://res.cloudinary.com/drpx4fqwg/image/upload/v1/user-images/ekpxsgqcwowbajdpk8bf
020eb046-57ef-4cba-81f8-caf1eadb5abf	a	https://res.cloudinary.com/drpx4fqwg/image/upload/v1/user-images/idwl4lvm52hofcli0kqd
63525976-697c-490b-a199-e29fb29f33b2	a	https://res.cloudinary.com/drpx4fqwg/image/upload/v1/user-images/ezwfihk89r9hyzixr3hd
03dbaa13-d3c4-4250-8952-aa30e01b4afb	a	https://res.cloudinary.com/drpx4fqwg/image/upload/v1/user-images/qic5usvseig6brj1dxqp
a5e14802-8430-47ef-9440-72c6ee8c7f72	a	https://res.cloudinary.com/drpx4fqwg/image/upload/v1/user-images/t848hwuyodm1z3ghphvb
0633553e-3e55-47e6-9d50-69e5d54dd343	a	https://res.cloudinary.com/drpx4fqwg/image/upload/v1/user-images/nejmf4dajxhrdkrhiyds
979baadb-7439-4a2a-a6ea-7eedaa06c1ee	a	https://res.cloudinary.com/drpx4fqwg/image/upload/v1/user-images/bjtl6kz0n8xdwbvbwg5q
3d4296a3-6eea-4f00-8c69-e81af0294825	a	https://res.cloudinary.com/drpx4fqwg/image/upload/v1/user-images/raz7ils3usq2rg4qpy91
9a9642d6-ab42-4e23-97e6-3d7faebc8b8b	a	https://res.cloudinary.com/drpx4fqwg/image/upload/v1/user-images/dnbfodrjpjof6biw64ch
0fc80006-4de7-42d7-83f3-227d71921744	a	https://res.cloudinary.com/drpx4fqwg/image/upload/v1/user-images/eqjx0yt8sf8cbesue5zz
49c85e3e-b8e5-4b83-99d6-e693132f74ae	a	https://res.cloudinary.com/drpx4fqwg/image/upload/v1/user-images/qeen9pcbto8aihqmfbdj
ddb64ad5-a468-4b7d-b4f8-088335f37297	a	https://res.cloudinary.com/drpx4fqwg/image/upload/v1/user-images/fnwaswqpspyq2xr2pyit
b66842d4-b3ac-4302-974a-d4b40fe030e6	aero swift	https://lh3.googleusercontent.com/a/ACg8ocKCK62U2fGZcHu_7Idd5NPQW_nOpom0hP8iJCna1fPq=s96-c
73468cda-4353-4b49-bbad-1a1e59c04bb7	aero swift	https://lh3.googleusercontent.com/a/ACg8ocKCK62U2fGZcHu_7Idd5NPQW_nOpom0hP8iJCna1fPq=s96-c
f2d09baa-2ae5-47fb-b648-e87916c1e0d3	aero swift	https://lh3.googleusercontent.com/a/ACg8ocKCK62U2fGZcHu_7Idd5NPQW_nOpom0hP8iJCna1fPq=s96-c
bb45364f-2420-4e13-868e-a1c3733d26ad	aero swift	https://lh3.googleusercontent.com/a/ACg8ocKCK62U2fGZcHu_7Idd5NPQW_nOpom0hP8iJCna1fPq=s96-c
4e1af4be-f6f3-4308-9a78-ace23755a511	aero swift	https://lh3.googleusercontent.com/a/ACg8ocKCK62U2fGZcHu_7Idd5NPQW_nOpom0hP8iJCna1fPq=s96-c
741db8fa-261e-4332-b616-9370b0111942	aero swift	https://lh3.googleusercontent.com/a/ACg8ocKCK62U2fGZcHu_7Idd5NPQW_nOpom0hP8iJCna1fPq=s96-c
e13a5c53-22d6-4a57-80fc-1f3f88f17cbe	a	https://res.cloudinary.com/drpx4fqwg/image/upload/v1/user-images/x78akpjnvsgpipxa0t9c
68618703-6bbc-4551-9ccc-34413df5c463	a	https://res.cloudinary.com/drpx4fqwg/image/upload/v1/user-images/jsffontwi3puqabpkzkc
53e86e83-db16-46a8-9474-527c6aa8bd97	wangeee	https://res.cloudinary.com/drpx4fqwg/image/upload/v1/user-images/kww4ddsenbdnbfmfs2sj
\.


--
-- Data for Name: otp; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.otp (uuid, email, created_time, otp_code, exp_time) FROM stdin;
\.


--
-- Data for Name: otp_forgot_password; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.otp_forgot_password (uuid, email, exp_time, created_time, otp_code) FROM stdin;
\.


--
-- Data for Name: otp_register; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.otp_register (uuid, email, exp_time, fullname, created_time, otp_code, password) FROM stdin;
\.


--
-- Data for Name: role; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.role (role_id, role_name) FROM stdin;
2ccf0499-c6ab-4c6f-9baa-97c1e3fd5285	USER
99808832-8146-4b68-b988-223134b0c2e7	ADMIN
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (is_active, created_at, role_id, user_id, email_address, fullname, password, image_id, bitrh_date, last_modified, phone_num) FROM stdin;
t	2024-01-15 19:52:17.964	\N	b9eb4a53-ea90-420b-bdf2-4426d05dcbd6	swiftaero290@gmail.com	aero swift	\N	741db8fa-261e-4332-b616-9370b0111942	\N	\N	\N
t	2024-01-15 15:16:17.734	2ccf0499-c6ab-4c6f-9baa-97c1e3fd5285	911ae253-eaee-4fdb-94d5-3d9aabfe82d3	airlanggapermana96@gmail.com	airlangga	$2a$10$VwO5JZFPx6MGCp.xsqU99.HAGN2xlG5bt5ShO5z23BKdAD3rTONAa	53e86e83-db16-46a8-9474-527c6aa8bd97	\N	\N	\N
t	2024-01-15 18:07:11.307	2ccf0499-c6ab-4c6f-9baa-97c1e3fd5285	bbc16f2f-1421-4205-b8b3-b60cdcaefedf	depanbelakangq944@gmail.com	John Doe	$2a$10$dl77cQocLm8O7GR1mp0FOONjNJDpII9JS/xCtTu0NI//GIy302mJi	68618703-6bbc-4551-9ccc-34413df5c463	1990-01-15 19:30:00	2024-01-16 11:54:14.715	6281294358181
\.


--
-- Name: image image_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.image
    ADD CONSTRAINT image_pkey PRIMARY KEY (image_id);


--
-- Name: otp_forgot_password otp_forgot_password_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.otp_forgot_password
    ADD CONSTRAINT otp_forgot_password_pkey PRIMARY KEY (uuid);


--
-- Name: otp otp_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.otp
    ADD CONSTRAINT otp_pkey PRIMARY KEY (uuid);


--
-- Name: otp_register otp_register_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.otp_register
    ADD CONSTRAINT otp_register_pkey PRIMARY KEY (uuid);


--
-- Name: role role_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role
    ADD CONSTRAINT role_pkey PRIMARY KEY (role_id);


--
-- Name: users uk_94dj9ry3k3tmcsyg8eatp7vvn; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk_94dj9ry3k3tmcsyg8eatp7vvn UNIQUE (image_id);


--
-- Name: otp uk_e0icwoc66jpmn78xh5iqq1j4q; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.otp
    ADD CONSTRAINT uk_e0icwoc66jpmn78xh5iqq1j4q UNIQUE (email);


--
-- Name: otp_register uk_j45w9qnt8j2bswht0k4vit0pl; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.otp_register
    ADD CONSTRAINT uk_j45w9qnt8j2bswht0k4vit0pl UNIQUE (email);


--
-- Name: otp_forgot_password uk_nw2869xri7pnfl20a4j22dw3i; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.otp_forgot_password
    ADD CONSTRAINT uk_nw2869xri7pnfl20a4j22dw3i UNIQUE (email);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (user_id);


--
-- Name: users fk4qu1gr772nnf6ve5af002rwya; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT fk4qu1gr772nnf6ve5af002rwya FOREIGN KEY (role_id) REFERENCES public.role(role_id);


--
-- Name: users fklqj25c28swu46s4jbudd7hore; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT fklqj25c28swu46s4jbudd7hore FOREIGN KEY (image_id) REFERENCES public.image(image_id);


--
-- PostgreSQL database dump complete
--

