-- *****************************************************************************
--
-- Model    : ${database.name}
-- Datenbank: ${dbmsName}
-- Version	: ${database.version}
-- Stand    : ${createDateStr}
--
-- *****************************************************************************
#if( $specificLines )

$specificLines
#end

--------------------------------------------------------------------------------
-- Tabellen l�schen                                                           --
--------------------------------------------------------------------------------
${dropTablesFormatter.format( $database )}


--------------------------------------------------------------------------------
-- Tabellen erstellen                                                         --
--------------------------------------------------------------------------------
#foreach( $entity in $database.entities )
${createTableFormatter.format( $entity )}

#end

-- ********************************* Dateiende *********************************