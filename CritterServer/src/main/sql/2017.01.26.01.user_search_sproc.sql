CREATE OR REPLACE FUNCTION usersearch(searchterm text)
  RETURNS SETOF users AS
$$
select *
from users where isactive = true and (username ILIKE searchterm or firstname ILIKE searchterm);
$$
  LANGUAGE sql VOLATILE
  COST 100;
ALTER FUNCTION usersearch(text)
  OWNER TO "Jeremy";
