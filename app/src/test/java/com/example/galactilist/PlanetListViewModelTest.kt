package com.example.galactilist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.galactilist.data.PlanetRepository
import com.example.galactilist.network.responses.Planet
import com.example.galactilist.network.responses.PlanetsResponse
import com.example.galactilist.ui.planetList.PlanetListViewModel
import com.example.galactilist.utils.Resource
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.argumentCaptor
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class PlanetListViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var planetRepository: PlanetRepository

    @Mock
    private lateinit var observer: Observer<Resource<List<Planet>>> // ✅ FIXED observer type

    private lateinit var viewModel: PlanetListViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher) // Set test dispatcher
        viewModel = PlanetListViewModel(planetRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset dispatcher after tests
    }

    @Test
    fun `perform get planet list success`() = runTest {
        // Arrange
        val planetList = listOf(
            Planet(
                name = "Tatooine",
                rotationPeriod = "23",
                orbitalPeriod = "304",
                diameter = "10465",
                climate = "arid",
                gravity = "1 standard",
                terrain = "desert",
                surfaceWater = "1",
                population = "200000",
                residents = listOf(
                    "https://swapi.dev/api/people/1/",
                    "https://swapi.dev/api/people/2/",
                    "https://swapi.dev/api/people/4/",
                    "https://swapi.dev/api/people/6/",
                    "https://swapi.dev/api/people/7/",
                    "https://swapi.dev/api/people/8/",
                    "https://swapi.dev/api/people/9/",
                    "https://swapi.dev/api/people/11/",
                    "https://swapi.dev/api/people/43/",
                    "https://swapi.dev/api/people/62/"
                ),
                films = listOf(
                    "https://swapi.dev/api/films/1/",
                    "https://swapi.dev/api/films/3/",
                    "https://swapi.dev/api/films/4/",
                    "https://swapi.dev/api/films/5/",
                    "https://swapi.dev/api/films/6/"
                ),
                created = "2014-12-09T13:50:49.641000Z",
                edited = "2014-12-20T20:58:18.411000Z",
                url = "https://swapi.dev/api/planets/1/"
            )
        )
        val response = PlanetsResponse(1, null, null, planetList)
        `when`(planetRepository.getPlanetList(1)).thenReturn(Resource.Success(response))

        // Observe LiveData
        viewModel.planetList.observeForever(observer)

        // Act
        viewModel.performGetPlanets()
        advanceUntilIdle() // Ensure coroutines complete

        // Assert
        verify(observer).onChanged(Resource.Loading) // First emits Loading
        verify(observer).onChanged(Resource.Success(planetList)) // Then emits Success
        assertTrue(viewModel.planetList.value is Resource.Success)

        // Cleanup
        viewModel.planetList.removeObserver(observer)
    }

    @Test
    fun `perform get planet list error`() = runTest {
        // Arrange
        val errorMessage = "Error"
        val error = Exception(errorMessage)

        `when`(planetRepository.getPlanetList(1)).thenReturn(Resource.Error(error))

        // Observe LiveData
        viewModel.planetList.observeForever(observer)

        // Act
        viewModel.performGetPlanets()
        advanceUntilIdle() // Ensure coroutines complete

        // Assert
        val argumentCaptor = argumentCaptor<Resource<List<Planet>>>()

        verify(observer).onChanged(Resource.Loading)

        verify(observer, times(2)).onChanged(argumentCaptor.capture())

        val capturedValues = argumentCaptor.allValues
        assertTrue(capturedValues[0] is Resource.Loading)
        assertTrue(capturedValues[1] is Resource.Error)

        val capturedError = capturedValues[1] as Resource.Error
        assertTrue(capturedError.exception is Exception)

        assertTrue(viewModel.planetList.value is Resource.Error)

        // Cleanup
        viewModel.planetList.removeObserver(observer)
    }
}