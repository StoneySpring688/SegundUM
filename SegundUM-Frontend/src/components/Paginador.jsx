import Pagination from 'react-bootstrap/Pagination';

function Paginador({ paginacion, onCambioPagina }) {
  // Si no hay paginación o solo hay 1 página, no mostramos nada
  if (!paginacion || paginacion.totalPages <= 1) {
    return null;
  }

  const { number: currentPage, totalPages } = paginacion;
  const lastPage = totalPages - 1; // Las páginas van de 0 a totalPages - 1

  // Lógica para mostrar la página actual, una anterior y una posterior
  let startPage = Math.max(0, currentPage - 1);
  let endPage = Math.min(lastPage, currentPage + 1);

  // Ajuste para mostrar siempre 3 números si es posible (ej: al estar en la página 1)
  if (currentPage === 0) {
    endPage = Math.min(lastPage, 2);
  } else if (currentPage === lastPage) {
    startPage = Math.max(0, lastPage - 2);
  }

  // Generamos el array de páginas centrales a renderizar
  const pages = [];
  for (let i = startPage; i <= endPage; i++) {
    pages.push(i);
  }

  // Función manejadora que evita peticiones a la misma página o páginas inválidas
  const handlePageClick = (pageIndex) => {
    if (pageIndex !== currentPage && pageIndex >= 0 && pageIndex <= lastPage) {
      onCambioPagina(pageIndex);
    }
  };

  return (
    // size="sm" y flex-wrap aseguran que se adapte perfectamente a viewports xs
    <Pagination size="sm" className="justify-content-center mt-4 flex-wrap">
      {/* Botones de inicio y retroceso */}
      <Pagination.First onClick={() => handlePageClick(0)} disabled={currentPage === 0} />
      <Pagination.Prev onClick={() => handlePageClick(currentPage - 1)} disabled={currentPage === 0} />

      {/* Si estamos muy lejos del inicio, mostramos el '1' y la elipsis */}
      {startPage > 0 && (
        <>
          <Pagination.Item onClick={() => handlePageClick(0)}>{1}</Pagination.Item>
          {startPage > 1 && <Pagination.Ellipsis disabled />}
        </>
      )}

      {/* Rango de páginas centrales */}
      {pages.map((pageIndex) => (
        <Pagination.Item
          key={pageIndex}
          active={pageIndex === currentPage}
          onClick={() => handlePageClick(pageIndex)}
        >
          {pageIndex + 1} {/* Sumamos 1 solo para la vista del usuario */}
        </Pagination.Item>
      ))}

      {/* Si estamos muy lejos del final, mostramos la elipsis y la última página */}
      {endPage < lastPage && (
        <>
          {endPage < lastPage - 1 && <Pagination.Ellipsis disabled />}
          <Pagination.Item onClick={() => handlePageClick(lastPage)}>
            {totalPages}
          </Pagination.Item>
        </>
      )}

      {/* Botones de avance y final */}
      <Pagination.Next onClick={() => handlePageClick(currentPage + 1)} disabled={currentPage === lastPage} />
      <Pagination.Last onClick={() => handlePageClick(lastPage)} disabled={currentPage === lastPage} />
    </Pagination>
  );
}

export default Paginador;