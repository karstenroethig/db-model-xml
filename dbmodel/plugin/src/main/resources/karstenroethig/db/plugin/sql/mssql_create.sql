-- *****************************************************************************
--
-- Model    : ${database.name}
-- Datenbank: MS SQL Server 2005
-- Version	: ${database.version}
-- Stand    : ${createDateStr}
--
-- *****************************************************************************

--------------------------------------------------------------------------------
-- Tabellen löschen                                                           --
--------------------------------------------------------------------------------
#foreach( $entity in $database.entities )
if object_id( '${entity.name}' ) > 0 drop table ${entity.name}
#end
go


--------------------------------------------------------------------------------
-- Tabellen erstellen                                                         --
--------------------------------------------------------------------------------
#foreach( $entity in $database.entities )
create table ${entity.name} (
	#foreach( $attribute in $entity.attributes )
	${attribute.name} ${datatypeFormatter.format( $attribute.datatype )} #if( $attribute.nullable ) null #else not null #end,
	#end
	-- TODO primary key contraint
)

#end
go

-- ********************************* Dateiende *********************************