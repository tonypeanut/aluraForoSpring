alter table usuarios
add column estado VARCHAR(45),
add column fecha_creacion DATETIME NULL,
add column fecha_actualizacion DATETIME NULL;

update usuarios SET estado = "Activo"
