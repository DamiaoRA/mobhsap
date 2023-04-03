-----------Função-----------
CREATE OR REPLACE FUNCTION public.regexlookbehind(i integer, seq text)
 RETURNS text
 LANGUAGE plpgsql
AS $function$
declare
   aspect text := '[+-]?\w[.]?\w*';
   regex text := '';
   result text;
begin
  for counter in reverse i..2 loop
    regex := regex || aspect || ',';
  end loop;
  regex := '(?<=(' || regex || '))' || aspect;

  SELECT substring(seq from regex) into result;

  return result;
end;
$function$
;
