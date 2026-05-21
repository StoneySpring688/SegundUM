import Pagination from 'react-bootstrap/Pagination';

function Paginador({ paginacion, onCambioPagina }) {
    
  if (!paginacion || paginacion.totalPages <= 1) {
    return null;
  }

  const { number: currentPage, totalPages } = paginacion;
  const lastPage = totalPages - 1;

  let startPage = Math.max(0, currentPage - 1);
  let endPage = Math.min(lastPage, currentPage + 1);

  if (currentPage === 0) {
    endPage = Math.min(lastPage, 2);
  } else if (currentPage === lastPage) {
    startPage = Math.max(0, lastPage - 2);
  }

  const pages = [];
  for (let i = startPage; i <= endPage; i++) {
    pages.push(i);
  }

  const handlePageClick = (pageIndex) => {
    if (pageIndex !== currentPage && pageIndex >= 0 && pageIndex <= lastPage) {
      onCambioPagina(pageIndex);
    }
  };

  return (
    <Pagination size="sm" className="justify-content-center mt-4 flex-wrap">
      {/* Botones de inicio y retroceso */}
      <Pagination.First onClick={() => handlePageClick(0)} disabled={currentPage === 0} />
      <Pagination.Prev onClick={() => handlePageClick(currentPage - 1)} disabled={currentPage === 0} />

      {/* si estamos muy lejos del inicio, mostramos el '1' y la elipsis */}
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